@echo off

:: Parámetros
set CONTAINER_NAME=%1
if "%CONTAINER_NAME%"=="" set CONTAINER_NAME=habitflow-db

set DB_USER=%2
if "%DB_USER%"=="" set DB_USER=directus

set DB_NAME=%3
if "%DB_NAME%"=="" set DB_NAME=directus

set DDL_FILE=%4
if "%DDL_FILE%"=="" set DDL_FILE=sql\DDL.sql

set DML_FILE=%5
if "%DML_FILE%"=="" set DML_FILE=sql\DML.sql

:: Copiar archivos SQL al contenedor
docker cp "%DDL_FILE%" "%CONTAINER_NAME%:/DDL.sql"
docker cp "%DML_FILE%" "%CONTAINER_NAME%:/DML.sql"

:: Ejecutar los scripts SQL
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /DDL.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /DML.sql