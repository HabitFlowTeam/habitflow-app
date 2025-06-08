CREATE VIEW CATEGORY_RANKING_VIEW AS
WITH MaxStreakPerUserCategory AS (
    SELECT 
        h.user_id,
        c.name as category_name,
        MAX(h.streak) as max_streak
    FROM habits h
    JOIN categories c ON h.category_id = c.id
    WHERE h.is_deleted = false
    GROUP BY h.user_id, c.name
),
RankedHabits AS (
    SELECT 
        m.user_id as id,
        p.full_name,
        m.max_streak as streak,
        p.avatar_url,
        m.category_name,
        -- Rank users within each category based on max streak
        ROW_NUMBER() OVER (
            PARTITION BY m.category_name 
            ORDER BY m.max_streak DESC
        ) as rank
    FROM MaxStreakPerUserCategory m
    JOIN profiles p ON m.user_id = p.id
)
SELECT 
    id,
    full_name,
    streak,
    avatar_url,
    category_name
FROM RankedHabits
WHERE rank <= 10
ORDER BY streak DESC; 