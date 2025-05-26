-- =======================================================
-- TRIGGER MEJORADO PARA CREAR ENTRADAS DIARIAS DE HABITS_TRACKING
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
    
    RAISE NOTICE 'Día de la semana calculado: % (1=Lunes, 7=Domingo)', current_day_of_week;
    
    -- Buscar el ID del día de la semana actual usando tus datos exactos
    SELECT id INTO current_week_day_id
    FROM week_days
    WHERE CASE 
        WHEN current_day_of_week = 1 THEN name = 'Lunes' OR abbreviation = 'LU'
        WHEN current_day_of_week = 2 THEN name = 'Martes' OR abbreviation = 'MA'
        WHEN current_day_of_week = 3 THEN name = 'Miércoles' OR abbreviation = 'MI'
        WHEN current_day_of_week = 4 THEN name = 'Jueves' OR abbreviation = 'JU'
        WHEN current_day_of_week = 5 THEN name = 'Viernes' OR abbreviation = 'VI'
        WHEN current_day_of_week = 6 THEN name = 'Sábado' OR abbreviation = 'SA'
        WHEN current_day_of_week = 7 THEN name = 'Domingo' OR abbreviation = 'DO'
    END
    LIMIT 1;
    
    IF current_week_day_id IS NULL THEN
        RAISE WARNING 'No se pudo determinar el ID del día de la semana para el día %', current_day_of_week;
        RETURN;
    END IF;
    
    RAISE NOTICE 'ID del día de la semana encontrado: %', current_week_day_id;
    
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

-- NUEVA FUNCIÓN: Crear entrada de tracking para un hábito específico si corresponde al día actual
CREATE OR REPLACE FUNCTION create_today_habit_tracking(p_habit_id UUID)
RETURNS void AS $$
DECLARE
    current_day_of_week INTEGER;
    current_week_day_id UUID;
    habit_exists BOOLEAN := FALSE;
BEGIN
    -- Verificar si el hábito existe y está activo
    SELECT EXISTS(
        SELECT 1 FROM habits 
        WHERE id = p_habit_id 
          AND is_deleted = FALSE 
          AND expiration_date >= CURRENT_DATE
    ) INTO habit_exists;
    
    IF NOT habit_exists THEN
        RAISE NOTICE 'Hábito % no existe o no está activo', p_habit_id;
        RETURN;
    END IF;
    
    -- Obtener el día de la semana actual (1=Lunes, 7=Domingo)
    current_day_of_week := EXTRACT(DOW FROM CURRENT_DATE);
    
    -- Ajustar para que Lunes sea 1 y Domingo sea 7 (PostgreSQL usa 0=Domingo)
    IF current_day_of_week = 0 THEN
        current_day_of_week := 7;
    END IF;
    
    -- Buscar el ID del día de la semana actual
    SELECT id INTO current_week_day_id
    FROM week_days
    WHERE CASE 
        WHEN current_day_of_week = 1 THEN name = 'Lunes' OR abbreviation = 'LU'
        WHEN current_day_of_week = 2 THEN name = 'Martes' OR abbreviation = 'MA'
        WHEN current_day_of_week = 3 THEN name = 'Miércoles' OR abbreviation = 'MI'
        WHEN current_day_of_week = 4 THEN name = 'Jueves' OR abbreviation = 'JU'
        WHEN current_day_of_week = 5 THEN name = 'Viernes' OR abbreviation = 'VI'
        WHEN current_day_of_week = 6 THEN name = 'Sábado' OR abbreviation = 'SA'
        WHEN current_day_of_week = 7 THEN name = 'Domingo' OR abbreviation = 'DO'
    END
    LIMIT 1;
    
    IF current_week_day_id IS NULL THEN
        RAISE WARNING 'No se pudo determinar el ID del día de la semana para el día %', current_day_of_week;
        RETURN;
    END IF;
    
    -- Verificar si el hábito debe ejecutarse hoy y crear entrada si no existe
    INSERT INTO habits_tracking (habit_id, tracking_date, is_checked)
    SELECT p_habit_id, CURRENT_DATE, FALSE
    WHERE EXISTS (
        SELECT 1 
        FROM habits_days hd 
        WHERE hd.habit_id = p_habit_id 
          AND hd.week_day_id = current_week_day_id
    )
    AND NOT EXISTS (
        SELECT 1 
        FROM habits_tracking ht 
        WHERE ht.habit_id = p_habit_id 
          AND ht.tracking_date = CURRENT_DATE
    );
    
    -- Log para debugging
    IF FOUND THEN
        RAISE NOTICE 'Entrada de tracking creada para hábito % en fecha %', p_habit_id, CURRENT_DATE;
    ELSE
        RAISE NOTICE 'No se creó entrada para hábito % - ya existe o no corresponde a hoy', p_habit_id;
    END IF;
                 
END;
$$ LANGUAGE plpgsql;

-- Crear la función que será llamada por el cron job (mantener la original)
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

-- Función trigger que verifica si es un nuevo día (mantener la original)
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

-- NUEVA FUNCIÓN TRIGGER: Para crear entrada del día actual cuando se crea/actualiza un hábito
CREATE OR REPLACE FUNCTION trigger_create_today_habit_tracking()
RETURNS TRIGGER AS $$
BEGIN
    -- Si es INSERT (nuevo hábito) y está activo
    IF TG_OP = 'INSERT' THEN
        IF NEW.is_deleted = FALSE AND NEW.expiration_date >= CURRENT_DATE THEN
            PERFORM create_today_habit_tracking(NEW.id);
        END IF;
        RETURN NEW;
    END IF;
    
    -- Si es UPDATE y cambió el estado de activo o la fecha de expiración
    IF TG_OP = 'UPDATE' THEN
        -- Si el hábito se activó o cambió su fecha de expiración
        IF (OLD.is_deleted = TRUE AND NEW.is_deleted = FALSE) OR
           (OLD.expiration_date < CURRENT_DATE AND NEW.expiration_date >= CURRENT_DATE) THEN
            PERFORM create_today_habit_tracking(NEW.id);
        END IF;
        RETURN NEW;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- NUEVA FUNCIÓN TRIGGER: Para crear entrada del día actual cuando se asigna un día a un hábito
CREATE OR REPLACE FUNCTION trigger_create_today_habit_day_tracking()
RETURNS TRIGGER AS $$
BEGIN
    -- Solo en INSERT (nueva asignación de día)
    IF TG_OP = 'INSERT' THEN
        PERFORM create_today_habit_tracking(NEW.habit_id);
        RETURN NEW;
    END IF;
    
    -- En UPDATE, verificar si cambió el día de la semana
    IF TG_OP = 'UPDATE' AND OLD.week_day_id != NEW.week_day_id THEN
        PERFORM create_today_habit_tracking(NEW.habit_id);
        RETURN NEW;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear triggers existentes (mantener los originales)
DROP TRIGGER IF EXISTS daily_habits_check_trigger ON habits;
CREATE TRIGGER daily_habits_check_trigger
    AFTER INSERT OR UPDATE ON habits
    FOR EACH ROW
    EXECUTE FUNCTION check_and_create_daily_tracking();

DROP TRIGGER IF EXISTS daily_habits_tracking_check_trigger ON habits_tracking;
CREATE TRIGGER daily_habits_tracking_check_trigger
    BEFORE INSERT ON habits_tracking
    FOR EACH ROW
    EXECUTE FUNCTION check_and_create_daily_tracking();

-- NUEVOS TRIGGERS: Para crear entradas del día actual automáticamente
DROP TRIGGER IF EXISTS habits_create_today_tracking_trigger ON habits;
CREATE TRIGGER habits_create_today_tracking_trigger
    AFTER INSERT OR UPDATE ON habits
    FOR EACH ROW
    EXECUTE FUNCTION trigger_create_today_habit_tracking();

DROP TRIGGER IF EXISTS habits_days_create_today_tracking_trigger ON habits_days;
CREATE TRIGGER habits_days_create_today_tracking_trigger
    AFTER INSERT OR UPDATE ON habits_days
    FOR EACH ROW
    EXECUTE FUNCTION trigger_create_today_habit_day_tracking();

-- OPCIÓN MANUAL: Función para ejecutar manualmente si lo prefieres (mantener la original)
CREATE OR REPLACE FUNCTION execute_daily_habit_creation()
RETURNS TEXT AS $$
BEGIN
    PERFORM create_daily_habit_tracking();
    RETURN 'Entradas de tracking creadas exitosamente para ' || CURRENT_DATE;
END;
$$ LANGUAGE plpgsql;

-- NUEVA FUNCIÓN MANUAL: Para crear entrada de un hábito específico para hoy
CREATE OR REPLACE FUNCTION execute_today_habit_creation(p_habit_id UUID)
RETURNS TEXT AS $$
BEGIN
    PERFORM create_today_habit_tracking(p_habit_id);
    RETURN 'Entrada de tracking verificada/creada para hábito ' || p_habit_id || ' en fecha ' || CURRENT_DATE;
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar manualmente: 
-- SELECT execute_daily_habit_creation(); -- Todos los hábitos del día
-- SELECT execute_today_habit_creation('uuid-del-habito'); -- Un hábito específico

-- COMENTARIO: Si tienes pg_cron disponible, puedes usar:
-- SELECT cron.schedule('daily-habits-tracking', '1 0 * * *', 'SELECT scheduled_daily_habit_tracking();');
