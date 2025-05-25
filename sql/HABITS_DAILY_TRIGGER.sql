-- =======================================================
-- TRIGGER PARA CREAR ENTRADAS DIARIAS DE HABITS_TRACKING
-- =======================================================

-- Función que crea las entradas diarias de tracking para hábitos activos
CREATE OR REPLACE FUNCTION create_daily_habit_tracking()
RETURNS void AS $$
DECLARE
    current_day_of_week INTEGER;
    current_week_day_id UUID;
BEGIN
    -- Obtener el día de la semana actual (1=Lunes, 7=Domingo)
    current_day_of_week := EXTRACT(DOW FROM CURRENT_DATE);
    
    -- Ajustar para que Lunes sea 1 y Domingo sea 7 (PostgreSQL usa 0=Domingo)
    IF current_day_of_week = 0 THEN
        current_day_of_week := 7;
    END IF;
    
    -- Obtener el ID del día de la semana actual
    -- Asumiendo que en week_days tienes los días ordenados: Lunes=1, Martes=2, etc.
    SELECT id INTO current_week_day_id 
    FROM week_days 
    WHERE id = (
        SELECT id 
        FROM week_days 
        ORDER BY name 
        OFFSET (current_day_of_week - 1) 
        LIMIT 1
    );
    
    -- Si no se puede determinar el día, usar una consulta alternativa
    IF current_week_day_id IS NULL THEN
        SELECT id INTO current_week_day_id
        FROM week_days
        WHERE CASE 
            WHEN current_day_of_week = 1 THEN name ILIKE '%lunes%' OR abbreviation ILIKE 'L%'
            WHEN current_day_of_week = 2 THEN name ILIKE '%martes%' OR abbreviation ILIKE 'M%'
            WHEN current_day_of_week = 3 THEN name ILIKE '%miércoles%' OR abbreviation ILIKE 'X%'
            WHEN current_day_of_week = 4 THEN name ILIKE '%jueves%' OR abbreviation ILIKE 'J%'
            WHEN current_day_of_week = 5 THEN name ILIKE '%viernes%' OR abbreviation ILIKE 'V%'
            WHEN current_day_of_week = 6 THEN name ILIKE '%sábado%' OR abbreviation ILIKE 'S%'
            WHEN current_day_of_week = 7 THEN name ILIKE '%domingo%' OR abbreviation ILIKE 'D%'
        END
        LIMIT 1;
    END IF;
    
    -- Insertar entradas de tracking para hábitos activos del día actual
    INSERT INTO habits_tracking (habit_id, tracking_date, is_checked)
    SELECT DISTINCT h.id, CURRENT_DATE, FALSE
    FROM habits h
    INNER JOIN habits_days hd ON h.id = hd.habit_id
    WHERE h.is_deleted = FALSE
      AND h.expiration_date >= CURRENT_DATE
      AND hd.week_day_id = current_week_day_id
      AND NOT EXISTS (
          SELECT 1 
          FROM habits_tracking ht 
          WHERE ht.habit_id = h.id 
            AND ht.tracking_date = CURRENT_DATE
      );
      
    -- Log para debugging (opcional)
    RAISE NOTICE 'Creadas entradas de tracking para la fecha: %, día de la semana: %', 
                 CURRENT_DATE, current_day_of_week;
                 
END;
$$ LANGUAGE plpgsql;

-- Crear la función que será llamada por el cron job
CREATE OR REPLACE FUNCTION scheduled_daily_habit_tracking()
RETURNS void AS $$
BEGIN
    PERFORM create_daily_habit_tracking();
END;
$$ LANGUAGE plpgsql;

-- Crear tabla auxiliar para simular eventos programados
CREATE TABLE IF NOT EXISTS daily_scheduler (
    id SERIAL PRIMARY KEY,
    last_execution_date DATE,
    task_name VARCHAR(100) UNIQUE NOT NULL
);

-- Insertar registro inicial (evitar duplicados)
INSERT INTO daily_scheduler (last_execution_date, task_name) 
VALUES (CURRENT_DATE - INTERVAL '1 day', 'daily_habits_tracking')
ON CONFLICT (task_name) DO NOTHING;

-- Función trigger que verifica si es un nuevo día
CREATE OR REPLACE FUNCTION check_and_create_daily_tracking()
RETURNS TRIGGER AS $$
DECLARE
    last_exec_date DATE;
BEGIN
    -- Obtener la última fecha de ejecución de forma segura
    SELECT last_execution_date INTO last_exec_date
    FROM daily_scheduler 
    WHERE task_name = 'daily_habits_tracking'
    LIMIT 1;
    
    -- Si no existe el registro o es un nuevo día, crear las entradas de tracking
    IF last_exec_date IS NULL OR last_exec_date < CURRENT_DATE THEN
        PERFORM create_daily_habit_tracking();
        
        -- Actualizar o insertar la fecha de última ejecución
        INSERT INTO daily_scheduler (last_execution_date, task_name)
        VALUES (CURRENT_DATE, 'daily_habits_tracking')
        ON CONFLICT (task_name) DO UPDATE SET
            last_execution_date = CURRENT_DATE;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear triggers solo si no existen (evitar errores en re-ejecuciones)
DROP TRIGGER IF EXISTS daily_habits_check_trigger ON habits;
CREATE TRIGGER daily_habits_check_trigger
    AFTER INSERT OR UPDATE ON habits
    FOR EACH ROW
    EXECUTE FUNCTION check_and_create_daily_tracking();

-- También crear un trigger en habits_tracking para verificar diariamente
DROP TRIGGER IF EXISTS daily_habits_tracking_check_trigger ON habits_tracking;
CREATE TRIGGER daily_habits_tracking_check_trigger
    BEFORE INSERT ON habits_tracking
    FOR EACH ROW
    EXECUTE FUNCTION check_and_create_daily_tracking();

-- OPCIÓN MANUAL: Función para ejecutar manualmente si lo prefieres
CREATE OR REPLACE FUNCTION execute_daily_habit_creation()
RETURNS TEXT AS $$
BEGIN
    PERFORM create_daily_habit_tracking();
    RETURN 'Entradas de tracking creadas exitosamente para ' || CURRENT_DATE;
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar manualmente: SELECT execute_daily_habit_creation();

-- COMENTARIO: Si tienes pg_cron disponible, puedes usar:
-- SELECT cron.schedule('daily-habits-tracking', '1 0 * * *', 'SELECT scheduled_daily_habit_tracking();');
