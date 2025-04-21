-- Eliminación de tablas existentes
DROP TABLE IF EXISTS articles_liked CASCADE;
DROP TABLE IF EXISTS articles_saved CASCADE;
DROP TABLE IF EXISTS habits_days CASCADE;
DROP TABLE IF EXISTS habits_tracking CASCADE;
DROP TABLE IF EXISTS roles_permissions CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS articles CASCADE;
DROP TABLE IF EXISTS habits CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS week_days CASCADE;

-- Tablas independientes
CREATE TABLE categories (
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE permissions (
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
    id   UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE users (
    id          UUID PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    username    VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    streak      INTEGER NOT NULL,
    best_streak INTEGER NOT NULL,
    avatar_url  VARCHAR(255),
    created_at  TIMESTAMP NOT NULL,
    CONSTRAINT user_unique UNIQUE (username, email)
);

CREATE TABLE week_days (
    id           UUID PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(10) NOT NULL
);

-- Tablas dependientes
CREATE TABLE articles (
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT NOT NULL,
    image_url   VARCHAR(255),
    is_deleted  BOOLEAN NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    user_id     UUID NOT NULL,
    category_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE articles_liked (
    user_id UUID NOT NULL,
    article_id   UUID NOT NULL,
    PRIMARY KEY (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE TABLE articles_saved (
    user_id    UUID NOT NULL,
    article_id UUID NOT NULL,
    PRIMARY KEY (user_id, article_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE TABLE habits (
    id                   UUID PRIMARY KEY,
    name                 VARCHAR(100) NOT NULL,
    streak               INTEGER NOT NULL,
    notifications_enable BOOLEAN NOT NULL,
    reminder_time        DATE,
    is_deleted           BOOLEAN NOT NULL,
    created_at           TIMESTAMP NOT NULL,
    expiration_date      DATE NOT NULL,
    category_id          UUID NOT NULL,
    user_id              UUID NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE habits_days (
    habit_id    UUID NOT NULL,
    week_day_id UUID NOT NULL,
    PRIMARY KEY (habit_id, week_day_id),
    FOREIGN KEY (habit_id) REFERENCES habits(id),
    FOREIGN KEY (week_day_id) REFERENCES week_days(id)
);

CREATE TABLE habits_tracking (
    id            UUID PRIMARY KEY,
    is_checked    BOOLEAN NOT NULL,
    tracking_date DATE NOT NULL,
    habit_id      UUID NOT NULL,
    FOREIGN KEY (habit_id) REFERENCES habits(id)
);

CREATE TABLE roles_permissions (
    role_id       UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
