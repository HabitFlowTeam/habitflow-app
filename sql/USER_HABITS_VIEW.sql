CREATE OR REPLACE VIEW active_user_habits AS
SELECT
    md5(h.user_id::text || h.id::text) AS id,
    h.id AS habit_id,
    h.user_id,
    h.name AS habit_name,
    h.streak,
    ARRAY(
        SELECT DISTINCT wd.name 
        FROM habits_days hd 
        JOIN week_days wd ON hd.week_day_id = wd.id 
        WHERE hd.habit_id = h.id
    ) AS scheduled_days,
    COALESCE(
        (SELECT TRUE FROM habits_tracking ht 
         WHERE ht.habit_id = h.id 
         AND ht.tracking_date = CURRENT_DATE 
         AND ht.is_checked = TRUE 
         LIMIT 1),
        FALSE
    ) AS is_checked_today,
    (SELECT ht.id 
     FROM habits_tracking ht 
     WHERE ht.habit_id = h.id 
     AND ht.tracking_date = CURRENT_DATE 
     LIMIT 1) AS habit_tracking_id
FROM habits h
WHERE 
    h.expiration_date >= CURRENT_DATE
    AND h.is_deleted = FALSE;
