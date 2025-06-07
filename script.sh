#!/bin/bash
# Parámetros
CONTAINER_NAME=${1:-habitflow-db}
DB_USER=${2:-directus}
DB_NAME=${3:-directus}
DDL_FILE=${4:-sql/DDL.sql}
USER_HABIT_CALENDAR_VIEW_FILE=${5:-sql/USER_HABIT_CALENDAR_VIEW.sql}
USER_HABITS_VIEW_FILE=${6:-sql/USER_HABITS_VIEW.sql}
HABITS_DAILY_TRIGGER_FILE=${7:-sql/HABITS_DAILY_TRIGGER.sql}
RANKED_ARTICLES_VIEW_FILE=${8:-sql/RANKED_ARTICLES_VIEW.sql}
USER_ARTICLES_VIEW_FILE=${9:-sql/USER_ARTICLES_VIEW.sql}
USER_HABIT_TRACKING_VIEW_FILE=${10:-sql/USER_HABIT_TRACKING_VIEW.sql}
USER_HABIT_CATEGORIES_VIEW_FILE=${11:-sql/USER_HABIT_CATEGORIES_VIEW.sql}
DML_FILE=${9:-sql/DML.sql}

# Copiar archivos SQL al contenedor
docker cp "$DDL_FILE" "$CONTAINER_NAME:/tmp/DDL.sql"
docker cp "$USER_HABIT_CALENDAR_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_habit_calendar_view.sql"
docker cp "$USER_HABITS_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_habits_view.sql"
docker cp "$RANKED_ARTICLES_VIEW_FILE" "$CONTAINER_NAME:/tmp/ranked_articles_view.sql"
docker cp "$HABITS_DAILY_TRIGGER_FILE" "$CONTAINER_NAME:/tmp/habits_daily_trigger.sql"
docker cp "$USER_ARTICLES_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_articles_view.sql"
docker cp "$USER_HABIT_TRACKING_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_habit_tracking_view.sql"
docker cp "$USER_HABIT_CATEGORIES_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_habit_categories_view.sql"
docker cp "$DML_FILE" "$CONTAINER_NAME:/tmp/DML.sql"

# Ejecutar los scripts SQL
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DDL.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/user_habit_calendar_view.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/user_habits_view.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/ranked_articles_view.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/habits_daily_trigger.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/user_habit_categories_view.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DML.sql
