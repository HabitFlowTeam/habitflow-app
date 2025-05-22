#!/bin/bash

# Parámetros
CONTAINER_NAME=${1:-habitflow-db}
DB_USER=${2:-directus}
DB_NAME=${3:-directus}
DDL_FILE=${4:-sql/DDL.sql}
USER_HABIT_CALENDAR_VIEW_FILE=${5:-sql/USER_HABIT_CALENDAR_VIEW.sql}
DML_FILE=${6:-sql/DML.sql}

# Copiar archivos SQL al contenedor
docker cp "$DDL_FILE" "$CONTAINER_NAME:/tmp/DDL.sql"
docker cp "$USER_HABIT_CALENDAR_VIEW_FILE" "$CONTAINER_NAME:/tmp/user_habit_calendar_view.sql"
docker cp "$DML_FILE" "$CONTAINER_NAME:/tmp/DML.sql"

# Ejecutar los scripts SQL
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DDL.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/user_habit_calendar_view.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /tmp/DML.sql