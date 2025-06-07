CREATE OR REPLACE VIEW USER_HABIT_CATEGORIES_VIEW AS
SELECT
    ht.id AS tracking_id,
    h.id AS habit_id,
    h.name AS habit_name,
    h.streak,
    h.notifications_enable,
    h.reminder_time,
    h.is_deleted,
    h.created_at,
    h.expiration_date,
    h.category_id,
    h.user_id,
    ht.is_checked,
    ht.tracking_date,
    c.id,
    c.name AS category_name
FROM
    habits_tracking ht
JOIN
    habits h ON ht.habit_id = h.id
JOIN
    categories c ON h.category_id = c.id;

