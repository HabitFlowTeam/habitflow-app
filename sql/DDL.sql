-- Eliminación de tablas existentes
DROP TABLE IF EXISTS articles_liked CASCADE;
DROP TABLE IF EXISTS articles_saved CASCADE;
DROP TABLE IF EXISTS habits_days CASCADE;
DROP TABLE IF EXISTS habits_tracking CASCADE;
DROP TABLE IF EXISTS articles CASCADE;
DROP TABLE IF EXISTS habits CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS profiles CASCADE;
DELETE FROM directus_users WHERE id NOT IN (SELECT id FROM directus_users WHERE first_name = 'Admin');
DELETE FROM directus_roles WHERE id NOT IN (SELECT id FROM directus_roles WHERE name = 'Administrator');
DROP TABLE IF EXISTS week_days CASCADE;
DROP TRIGGER IF EXISTS daily_habits_check_trigger ON habits;
DROP TRIGGER IF EXISTS daily_habits_tracking_check_trigger ON habits_tracking;
DROP TABLE IF EXISTS daily_scheduler CASCADE;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE categories (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE week_days (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(10) NOT NULL
);

CREATE TABLE profiles (
    id              UUID PRIMARY KEY REFERENCES directus_users(id) ON DELETE CASCADE,
    full_name       VARCHAR(255),
    username        VARCHAR(100) UNIQUE,
    streak          INTEGER DEFAULT 0,
    best_streak     INTEGER DEFAULT 0,
    avatar_url      VARCHAR(255),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE articles (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title       VARCHAR(255) NOT NULL,
    content     TEXT NOT NULL,
    image_url   VARCHAR(255),
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id     UUID NOT NULL,
    category_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES directus_users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE articles_liked (
    user_id     UUID NOT NULL,
    article_id  UUID NOT NULL,
    PRIMARY KEY (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES directus_users(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE articles_saved (
    user_id    UUID NOT NULL,
    article_id UUID NOT NULL,
    PRIMARY KEY (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES directus_users(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE habits (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                 VARCHAR(100) NOT NULL,
    streak               INTEGER NOT NULL DEFAULT 0,
    notifications_enable BOOLEAN NOT NULL DEFAULT FALSE,
    reminder_time        TIME,
    is_deleted           BOOLEAN NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiration_date DATE NOT NULL DEFAULT (CURRENT_DATE + INTERVAL '1 year'),
    category_id          UUID NOT NULL,
    user_id              UUID NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES directus_users(id) ON DELETE CASCADE
);

CREATE TABLE habits_days (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    habit_id    UUID NOT NULL,
    week_day_id UUID NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    FOREIGN KEY (week_day_id) REFERENCES week_days(id),
    CONSTRAINT habits_days_unique UNIQUE (habit_id, week_day_id)
);

CREATE TABLE habits_tracking (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    is_checked    BOOLEAN NOT NULL DEFAULT FALSE,
    tracking_date DATE NOT NULL,
    habit_id      UUID NOT NULL,
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE
);
