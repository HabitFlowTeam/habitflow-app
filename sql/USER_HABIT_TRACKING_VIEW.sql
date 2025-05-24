CREATE OR REPLACE VIEW USER_HABIT_TRACKING_VIEW AS
SELECT
    ht.id AS habit_id,
    h.id AS id,
    h.user_id,
    ht.is_checked,
    ht.tracking_date
FROM
    habits_tracking ht
JOIN
    habits h ON ht.habit_id = h.id;

