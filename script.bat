@echo off
setlocal EnableDelayedExpansion

:: Parámetros
set CONTAINER_NAME=%1
if "%CONTAINER_NAME%"=="" set CONTAINER_NAME=habitflow-db

set DB_USER=%2
if "%DB_USER%"=="" set DB_USER=directus

set DB_NAME=%3
if "%DB_NAME%"=="" set DB_NAME=directus

:: Archivos SQL con rutas por defecto
set DDL_FILE=sql\DDL.sql
set USER_HABIT_CALENDAR_VIEW_FILE=sql\USER_HABIT_CALENDAR_VIEW.sql
set USER_HABITS_VIEW_FILE=sql\USER_HABITS_VIEW.sql
set HABITS_DAILY_TRIGGER_FILE=sql\HABITS_DAILY_TRIGGER.sql
set RANKED_ARTICLES_VIEW_FILE=sql\RANKED_ARTICLES_VIEW.sql
set CATEGORY_RANKING_VIEW_FILE=sql\CATEGORY_RANKING_VIEW.sql
set DML_FILE=sql\DML.sql
set USER_ARTICLES_VIEW_FILE=sql\USER_ARTICLES_VIEW.sql
set USER_HABIT_TRACKING_VIEW_FILE=sql\USER_HABIT_TRACKING_VIEW.sql
set USER_HABIT_CATEGORIES_VIEW_FILE=sql\USER_HABIT_CATEGORIES_VIEW.sql

:: Copiar archivos SQL al contenedor
docker cp "%DDL_FILE%" "%CONTAINER_NAME%:/tmp/DDL.sql"
docker cp "%USER_HABIT_CALENDAR_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/USER_HABIT_CALENDAR_VIEW.sql"
docker cp "%USER_HABITS_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/USER_HABITS_VIEW.sql"
docker cp "%HABITS_DAILY_TRIGGER_FILE%" "%CONTAINER_NAME%:/tmp/HABITS_DAILY_TRIGGER.sql"
docker cp "%RANKED_ARTICLES_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/RANKED_ARTICLES_VIEW.sql"
docker cp "%CATEGORY_RANKING_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/CATEGORY_RANKING_VIEW.sql"
docker cp "%DML_FILE%" "%CONTAINER_NAME%:/tmp/DML.sql"
docker cp "%USER_ARTICLES_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/USER_ARTICLES_VIEW.sql"
docker cp "%USER_HABIT_TRACKING_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/USER_HABIT_TRACKING_VIEW.sql"
docker cp "%USER_HABIT_CATEGORIES_VIEW_FILE%" "%CONTAINER_NAME%:/tmp/USER_HABIT_CATEGORIES_VIEW.sql"

:: Ejecutar los scripts SQL
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/DDL.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/USER_HABIT_CALENDAR_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/USER_HABITS_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/HABITS_DAILY_TRIGGER.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/RANKED_ARTICLES_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/CATEGORY_RANKING_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/DML.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/USER_ARTICLES_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/USER_HABIT_TRACKING_VIEW.sql
docker exec -it "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -f /tmp/USER_HABIT_CATEGORIES_VIEW.sql
