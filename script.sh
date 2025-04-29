#!/bin/bash

# Parámetros
CONTAINER_NAME=${1:-habitflow-db}
DB_USER=${2:-directus}
DB_NAME=${3:-directus}
DDL_FILE=${4:-sql/DDL.sql}
DML_FILE=${5:-sql/DML.sql}

# Copiar archivos SQL al contenedor
docker cp "$DDL_FILE" "$CONTAINER_NAME:/DDL.sql"
docker cp "$DML_FILE" "$CONTAINER_NAME:/DML.sql"

# Ejecutar los scripts SQL
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /DDL.sql
docker exec -it "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -f /DML.sql