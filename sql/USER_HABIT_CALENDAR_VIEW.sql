CREATE OR REPLACE VIEW user_habit_calendar_view AS
WITH date_range AS (
    SELECT 
        generate_series(
            COALESCE(
                (SELECT MIN(created_at)::date FROM habits),  -- Paréntesis corregido aquí
                CURRENT_DATE - INTERVAL '30 days'
            ),
            CURRENT_DATE,
            '1 day'::interval
        )::date AS calendar_date
),
user_dates AS (
    SELECT
        u.id AS user_id,
        dr.calendar_date,
        CASE EXTRACT(DOW FROM dr.calendar_date)
            WHEN 1 THEN 'Lunes'
            WHEN 2 THEN 'Martes'
            WHEN 3 THEN 'Miércoles'
            WHEN 4 THEN 'Jueves'
            WHEN 5 THEN 'Viernes'
            WHEN 6 THEN 'Sábado'
            WHEN 0 THEN 'Domingo'
        END AS spanish_day_name
    FROM directus_users u
    CROSS JOIN date_range dr
),
habit_schedules AS (
    SELECT
        h.id AS habit_id,
        h.user_id,
        h.created_at,
        h.expiration_date,
        h.is_deleted,
        wd.name AS scheduled_day
    FROM habits h
    JOIN habits_days hd ON h.id = hd.habit_id
    JOIN week_days wd ON hd.week_day_id = wd.id
),
habit_activity AS (
    SELECT
        ud.user_id,
        ud.calendar_date,
        COUNT(DISTINCT hs.habit_id) FILTER (
            WHERE
                hs.created_at::date <= ud.calendar_date
                AND hs.expiration_date >= ud.calendar_date
                AND hs.is_deleted = false
        ) AS total_scheduled_habits,
        COUNT(DISTINCT hs.habit_id) FILTER (
            WHERE
                hs.created_at::date <= ud.calendar_date
                AND hs.expiration_date >= ud.calendar_date
                AND hs.is_deleted = false
                AND EXISTS (
                    SELECT 1 FROM habits_tracking ht
                    WHERE ht.habit_id = hs.habit_id
                    AND ht.tracking_date = ud.calendar_date
                    AND ht.is_checked = true
                )
        ) AS completed_habits
    FROM user_dates ud
    LEFT JOIN habit_schedules hs 
        ON ud.user_id = hs.user_id 
        AND ud.spanish_day_name = hs.scheduled_day
    GROUP BY ud.user_id, ud.calendar_date
)
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
    CASE
        WHEN total_scheduled_habits = 0 THEN 0
        ELSE ROUND((completed_habits::float / total_scheduled_habits::float) * 100)
    END AS completion_percentage
FROM habit_activity
ORDER BY user_id, calendar_date;
