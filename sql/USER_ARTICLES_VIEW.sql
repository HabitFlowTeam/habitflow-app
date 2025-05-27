CREATE OR REPLACE VIEW user_articles_view AS
SELECT
    a.*,
    COUNT(al.user_id) AS likes_count
FROM
    articles a
LEFT JOIN
    articles_liked al
ON
    a.id = al.article_id
GROUP BY
    a.id;