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

set USER_HABIT_CALENDAR_VIEW_FILE=%5
if "%USER_HABIT_CALENDAR_VIEW_FILE%"=="" set USER_HABIT_CALENDAR_VIEW_FILE=sql\USER_HABIT_CALENDAR_VIEW.sql

set DML_FILE=%6
if "%DML_FILE%"=="" set DML_FILE=sql\DML.sql

:: Copiar archivos SQL al contenedor
docker cp "%DDL_FILE%" "%CONTAINER_NAME%:/DDL.sql"
docker cp "%USER_HABIT_CALENDAR_VIEW_FILE%" "%CONTAINER_NAME%:/USER_HABIT_CALENDAR_VIEW.sql"
docker cp "%DML_FILE%" "%CONTAINER_NAME%:/DML.sql"

:: Ejecutar los scripts SQL
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /DDL.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /USER_HABIT_CALENDAR_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /DML.sql