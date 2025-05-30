CREATE OR REPLACE VIEW RANKED_ARTICLES_VIEW AS
SELECT
    a.id AS id,
    a.title,
    a.content,
    a.image_url,
    a.category_id,
    a.user_id,
    p.username AS author_name,
    p.avatar_url AS author_image_url,
    COUNT(al.user_id) AS likes_count
FROM articles a
JOIN profiles p ON a.user_id = p.id
LEFT JOIN articles_liked al ON a.id = al.article_id
WHERE a.is_deleted = false
GROUP BY a.id, p.username, p.avatar_url;

