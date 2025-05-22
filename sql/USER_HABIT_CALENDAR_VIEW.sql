CREATE OR REPLACE VIEW user_habit_calendar_view AS
WITH date_range AS (
    -- Generar todas las fechas desde la primera creación de hábito hasta la fecha actual
    SELECT 
        generate_series(
            COALESCE((SELECT MIN(created_at)::date FROM habits), CURRENT_DATE - INTERVAL '30 days'),
            CURRENT_DATE,
            '1 day'::interval
        )::date AS calendar_date
),
user_dates AS (
    -- Combinar usuarios con todas las fechas
    SELECT
        u.id AS user_id,
        dr.calendar_date
    FROM
        directus_users u
    CROSS JOIN
        date_range dr
),
habit_schedules AS (
    -- Simplemente obtener todos los hábitos para todos los usuarios
    SELECT
        h.id AS habit_id,
        h.user_id,
        h.name AS habit_name,
        h.created_at,
        h.expiration_date,
        h.is_deleted
    FROM
        habits h
),
habit_activity AS (
    -- Para cada usuario y fecha, calcular la actividad de hábitos
    SELECT
        ud.user_id,
        ud.calendar_date,
        -- Total de hábitos activos para este día
        COUNT(DISTINCT hs.habit_id) FILTER (
            WHERE
                hs.user_id = ud.user_id
                AND hs.created_at::date <= ud.calendar_date
                AND hs.expiration_date >= ud.calendar_date
                AND hs.is_deleted = false
        ) AS total_scheduled_habits,
        -- Total de hábitos completados en este día
        COUNT(DISTINCT hs.habit_id) FILTER (
            WHERE
                hs.user_id = ud.user_id
                AND hs.created_at::date <= ud.calendar_date
                AND hs.expiration_date >= ud.calendar_date
                AND hs.is_deleted = false
                -- Y verificamos si fueron marcados como completados
                AND EXISTS (
                    SELECT 1 FROM habits_tracking ht
                    WHERE ht.habit_id = hs.habit_id
                    AND ht.tracking_date = ud.calendar_date
                    AND ht.is_checked = true
                )
        ) AS completed_habits
    FROM
        user_dates ud
    LEFT JOIN
        habit_schedules hs ON hs.user_id = ud.user_id
    GROUP BY
        ud.user_id, ud.calendar_date
)
-- Resultados finales con estado calculado
SELECT
    md5(user_id::text || calendar_date::text) AS id,
    user_id,
    calendar_date,
    total_scheduled_habits,
    completed_habits,
    CASE
        WHEN total_scheduled_habits = 0 THEN 'no_habits'
        WHEN total_scheduled_habits = completed_habits THEN 'all_completed'
        WHEN completed_habits > 0 THEN 'partially_completed'
        ELSE 'none_completed'
    END AS completion_status,
    -- Porcentaje de completitud
    CASE
        WHEN total_scheduled_habits = 0 THEN 0
        ELSE ROUND((completed_habits::float / total_scheduled_habits::float) * 100)
    END AS completion_percentage
FROM
    habit_activity
ORDER BY 
    user_id, calendar_date;