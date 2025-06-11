-- =======================================================
-- FUNCIONES SQL PARA GESTIÓN DE RACHAS DE HÁBITOS
-- =======================================================

-- Función para calcular la racha consecutiva de un hábito
CREATE OR REPLACE FUNCTION calculate_habit_streak(p_habit_id UUID, p_from_date DATE DEFAULT CURRENT_DATE)
RETURNS INTEGER AS $$
DECLARE
    streak_count INTEGER := 0;
    current_check_date DATE := p_from_date;
    is_scheduled BOOLEAN;
    is_completed BOOLEAN;
    current_day_name TEXT;
BEGIN
    -- Bucle para contar días consecutivos hacia atrás
    LOOP
        -- Obtener el nombre del día de la semana en español
        current_day_name := CASE EXTRACT(DOW FROM current_check_date)
            WHEN 1 THEN 'Lunes'
            WHEN 2 THEN 'Martes'
            WHEN 3 THEN 'Miércoles'
            WHEN 4 THEN 'Jueves'
            WHEN 5 THEN 'Viernes'
            WHEN 6 THEN 'Sábado'
            WHEN 0 THEN 'Domingo'
        END;
        
        -- Verificar si el hábito está programado para este día
        SELECT EXISTS (
            SELECT 1 
            FROM habits_days hd
            JOIN week_days wd ON hd.week_day_id = wd.id
            WHERE hd.habit_id = p_habit_id 
            AND wd.name = current_day_name
        ) INTO is_scheduled;
        
        -- Si no está programado para este día, ir al día anterior
        IF NOT is_scheduled THEN
            current_check_date := current_check_date - INTERVAL '1 day';
            CONTINUE;
        END IF;
        
        -- Verificar si fue completado en esta fecha
        SELECT EXISTS (
            SELECT 1 
            FROM habits_tracking ht
            WHERE ht.habit_id = p_habit_id 
            AND ht.tracking_date = current_check_date
            AND ht.is_checked = TRUE
        ) INTO is_completed;
        
        -- Si fue completado, incrementar la racha y continuar
        IF is_completed THEN
            streak_count := streak_count + 1;
            current_check_date := current_check_date - INTERVAL '1 day';
        ELSE
            -- La racha se rompe
            EXIT;
        END IF;
        
        -- Límite de seguridad para evitar bucles infinitos
        IF streak_count > 1000 THEN
            EXIT;
        END IF;
    END LOOP;
    
    RETURN streak_count;
END;
$$ LANGUAGE plpgsql;

-- Función para actualizar la racha de un hábito
CREATE OR REPLACE FUNCTION update_habit_streak(p_habit_id UUID)
RETURNS INTEGER AS $$
DECLARE
    new_streak INTEGER;
BEGIN
    -- Calcular la nueva racha
    new_streak := calculate_habit_streak(p_habit_id);
    
    -- Actualizar la racha en la tabla habits
    UPDATE habits 
    SET streak = new_streak 
    WHERE id = p_habit_id;
    
    RAISE NOTICE 'Updated streak for habit % to %', p_habit_id, new_streak;
    
    RETURN new_streak;
END;
$$ LANGUAGE plpgsql;

-- Función trigger que se ejecuta cuando se actualiza el estado de tracking
CREATE OR REPLACE FUNCTION trigger_update_habit_streak()
RETURNS TRIGGER AS $$
BEGIN
    -- Actualizar la racha del hábito cuando cambia el estado de tracking
    IF TG_OP = 'INSERT' OR (TG_OP = 'UPDATE' AND OLD.is_checked != NEW.is_checked) THEN
        PERFORM update_habit_streak(NEW.habit_id);
    END IF;
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Función para resetear rachas de hábitos perdidos (ejecutar diariamente)
CREATE OR REPLACE FUNCTION reset_missed_habit_streaks(p_check_date DATE DEFAULT CURRENT_DATE - INTERVAL '1 day')
RETURNS TABLE(habit_id UUID, old_streak INTEGER, reset_reason TEXT) AS $$
DECLARE
    habit_record RECORD;
    yesterday_day_name TEXT;
    was_scheduled BOOLEAN;
    was_completed BOOLEAN;
BEGIN
    -- Obtener el nombre del día a verificar
    yesterday_day_name := CASE EXTRACT(DOW FROM p_check_date)
        WHEN 1 THEN 'Lunes'
        WHEN 2 THEN 'Martes'
        WHEN 3 THEN 'Miércoles'
        WHEN 4 THEN 'Jueves'
        WHEN 5 THEN 'Viernes'
        WHEN 6 THEN 'Sábado'
        WHEN 0 THEN 'Domingo'
    END;
    
    -- Revisar todos los hábitos activos
    FOR habit_record IN 
        SELECT h.id, h.streak, h.name
        FROM habits h
        WHERE h.is_deleted = FALSE 
        AND h.expiration_date >= p_check_date
        AND h.streak > 0
    LOOP
        -- Verificar si el hábito estaba programado para el día a verificar
        SELECT EXISTS (
            SELECT 1 
            FROM habits_days hd
            JOIN week_days wd ON hd.week_day_id = wd.id
            WHERE hd.habit_id = habit_record.id 
            AND wd.name = yesterday_day_name
        ) INTO was_scheduled;
        
        -- Solo proceder si estaba programado
        IF was_scheduled THEN
            -- Verificar si fue completado
            SELECT EXISTS (
                SELECT 1 
                FROM habits_tracking ht
                WHERE ht.habit_id = habit_record.id 
                AND ht.tracking_date = p_check_date
                AND ht.is_checked = TRUE
            ) INTO was_completed;
            
            -- Si no fue completado, resetear la racha
            IF NOT was_completed THEN
                UPDATE habits 
                SET streak = 0 
                WHERE id = habit_record.id;
                
                -- Retornar información del reset
                habit_id := habit_record.id;
                old_streak := habit_record.streak;
                reset_reason := 'Missed scheduled day: ' || yesterday_day_name || ' on ' || p_check_date;
                
                RETURN NEXT;
                
                RAISE NOTICE 'Reset streak for habit % (%) from % to 0 - missed %', 
                    habit_record.id, habit_record.name, habit_record.streak, yesterday_day_name;
            END IF;
        END IF;
    END LOOP;
    
    RETURN;
END;
$$ LANGUAGE plpgsql;

-- Crear el trigger para actualizar rachas automáticamente
DROP TRIGGER IF EXISTS streak_update_trigger ON habits_tracking;
CREATE TRIGGER streak_update_trigger
    AFTER INSERT OR UPDATE ON habits_tracking
    FOR EACH ROW
    EXECUTE FUNCTION trigger_update_habit_streak();

-- Función para ejecutar manualmente el reset de rachas perdidas
CREATE OR REPLACE FUNCTION execute_missed_streaks_reset(p_date DATE DEFAULT CURRENT_DATE - INTERVAL '1 day')
RETURNS TEXT AS $$
DECLARE
    reset_count INTEGER;
    result_text TEXT;
BEGIN
    -- Ejecutar el reset y contar resultados
    SELECT COUNT(*) INTO reset_count
    FROM reset_missed_habit_streaks(p_date);
    
    result_text := 'Reset completed for date ' || p_date || '. ' || reset_count || ' habit streaks were reset.';
    
    RAISE NOTICE '%', result_text;
    
    RETURN result_text;
END;
$$ LANGUAGE plpgsql;

-- Función de utilidad para obtener la racha actual de un hábito
CREATE OR REPLACE FUNCTION get_habit_current_streak(p_habit_id UUID)
RETURNS INTEGER AS $$
DECLARE
    current_streak INTEGER;
BEGIN
    SELECT streak INTO current_streak
    FROM habits
    WHERE id = p_habit_id;
    
    RETURN COALESCE(current_streak, 0);
END;
$$ LANGUAGE plpgsql;

-- Vista para facilitar consultas de rachas
CREATE OR REPLACE VIEW habit_streak_status AS
SELECT 
    h.id as habit_id,
    h.name as habit_name,
    h.user_id,
    h.streak as current_streak,
    h.created_at,
    h.expiration_date,
    ARRAY(
        SELECT wd.name 
        FROM habits_days hd 
        JOIN week_days wd ON hd.week_day_id = wd.id 
        WHERE hd.habit_id = h.id
        ORDER BY 
            CASE wd.name
                WHEN 'Lunes' THEN 1
                WHEN 'Martes' THEN 2
                WHEN 'Miércoles' THEN 3
                WHEN 'Jueves' THEN 4
                WHEN 'Viernes' THEN 5
                WHEN 'Sábado' THEN 6
                WHEN 'Domingo' THEN 7
            END
    ) as scheduled_days,
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM habits_tracking ht 
            WHERE ht.habit_id = h.id 
            AND ht.tracking_date = CURRENT_DATE 
            AND ht.is_checked = TRUE
        ) THEN TRUE
        ELSE FALSE
    END as completed_today
FROM habits h
WHERE h.is_deleted = FALSE 
AND h.expiration_date >= CURRENT_DATE;

-- Ejemplos de uso:

-- Para recalcular la racha de un hábito específico:
-- SELECT update_habit_streak('uuid-del-habito');

-- Para obtener la racha actual de un hábito:
-- SELECT get_habit_current_streak('uuid-del-habito');

-- Para ejecutar el reset de rachas perdidas manualmente:
-- SELECT execute_missed_streaks_reset();

-- Para ver el estado de las rachas:
-- SELECT * FROM habit_streak_status WHERE user_id = 'uuid-del-usuario';

-- Para resetear rachas de una fecha específica:
-- SELECT * FROM reset_missed_habit_streaks('2025-05-20');

COMMENT ON FUNCTION calculate_habit_streak IS 'Calcula la racha consecutiva de un hábito desde una fecha específica hacia atrás';
COMMENT ON FUNCTION update_habit_streak IS 'Actualiza la racha de un hábito basándose en su historial de completado';
COMMENT ON FUNCTION reset_missed_habit_streaks IS 'Resetea las rachas de hábitos que no fueron completados en días programados';
COMMENT ON VIEW habit_streak_status IS 'Vista que muestra el estado actual de las rachas de todos los hábitos activos';
