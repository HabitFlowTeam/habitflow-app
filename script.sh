#!/bin/bash

# Parámetros
CONTAINER_NAME=${1:-habitflow-db}
DB_USER=${2:-directus}
DB_NAME=${3:-directus}

# Archivos SQL con rutas por defecto usando parámetros numerados
DDL_FILE=${4:-sql/DDL.sql}
USER_HABIT_CALENDAR_VIEW_FILE=${5:-sql/USER_HABIT_CALENDAR_VIEW.sql}
USER_HABITS_VIEW_FILE=${6:-sql/USER_HABITS_VIEW.sql}
HABITS_DAILY_TRIGGER_FILE=${7:-sql/HABITS_DAILY_TRIGGER.sql}
RANKED_ARTICLES_VIEW_FILE=${8:-sql/RANKED_ARTICLES_VIEW.sql}
CATEGORY_RANKING_VIEW_FILE=${9:-sql/CATEGORY_RANKING_VIEW.sql}

# Parámetro 10 (equivalente al shift y %1 en .bat)
DML_FILE=${10:-sql/DML.sql}

# Archivos con rutas fijas
USER_ARTICLES_VIEW_FILE=sql/USER_ARTICLES_VIEW.sql
USER_HABIT_TRACKING_VIEW_FILE=sql/USER_HABIT_TRACKING_VIEW.sql
USER_HABIT_CATEGORIES_VIEW_FILE=sql/USER_HABIT_CATEGORIES_VIEW.sql

# Copiar archivos SQL al contenedor
docker cp "$DDL_FILE" "$CONTAINER_NAME:/tmp/DDL.sql"
docker cp "$USER_HABIT_CALENDAR_VIEW_FILE" "$CONTAINER_NAME:/tmp/USER_HABIT_CALENDAR_VIEW.sql"
docker cp "$USER_HABITS_VIEW_FILE" "$CONTAINER_NAME:/tmp/USER_HABITS_VIEW.sql"
docker cp "$HABITS_DAILY_TRIGGER_FILE" "$CONTAINER_NAME:/tmp/HABITS_DAILY_TRIGGER.sql"
docker cp "$RANKED_ARTICLES_VIEW_FILE" "$CONTAINER_NAME:/tmp/RANKED_ARTICLES_VIEW.sql"
docker cp "$CATEGORY_RANKING_VIEW_FILE" "$CONTAINER_NAME:/tmp/CATEGORY_RANKING_VIEW.sql"
docker cp "$USER_ARTICLES_VIEW_FILE" "$CONTAINER_NAME:/tmp/USER_ARTICLES_VIEW.sql"
docker cp "$USER_HABIT_TRACKING_VIEW_FILE" "$CONTAINER_NAME:/tmp/USER_HABIT_TRACKING_VIEW.sql"
docker cp "$USER_HABIT_CATEGORIES_VIEW_FILE" "$CONTAINER_NAME:/tmp/USER_HABIT_CATEGORIES_VIEW.sql"
docker cp "$DML_FILE" "$CONTAINER_NAME:/tmp/DML.sql"

# Ejecutar los scripts SQL
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DDL.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/USER_HABIT_CALENDAR_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/USER_HABITS_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/HABITS_DAILY_TRIGGER.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/RANKED_ARTICLES_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/CATEGORY_RANKING_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/USER_ARTICLES_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/USER_HABIT_TRACKING_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/USER_HABIT_CATEGORIES_VIEW.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DML.sql