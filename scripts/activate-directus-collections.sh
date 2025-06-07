#!/bin/bash

echo "$(date '+%Y-%m-%d %H:%M:%S') Esperando que Directus esté completamente inicializado..."

# Esperar un tiempo adicional para asegurar que Directus esté completamente listo
sleep 3

# 1) Verificar que Directus responda correctamente
MAX_RETRIES=10
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
  PING_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8055/server/ping)

  if [ "$PING_STATUS" = "200" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') Directus está respondiendo correctamente."
    break
  else
    RETRY_COUNT=$((RETRY_COUNT + 1))
    echo "$(date '+%Y-%m-%d %H:%M:%S') Intento $RETRY_COUNT/$MAX_RETRIES: Directus aún no responde correctamente (status: $PING_STATUS)... esperando 5s"
    sleep 5
  fi

  if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') ERROR: Directus no responde después de $MAX_RETRIES intentos."
    exit 1
  fi
done

echo "$(date '+%Y-%m-%d %H:%M:%S') Obteniendo token de Admin..."

# 2) Login para obtener token
resp=$(curl -s -X POST http://localhost:8055/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"habitflow@gmail.com","password":"habitflow"}')

# 3) Extraer access_token
TOKEN=$(echo "$resp" | grep -o '"access_token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "ERROR: no se obtuvo token. Respuesta completa:"
  echo "$resp"
  exit 1
fi

echo "$(date '+%Y-%m-%d %H:%M:%S') Token obtenido: ${TOKEN:0:10}... (parcialmente oculto)"

# Lista de todas las colecciones para activar
COLLECTIONS=(
  "categories"
  "week_days"
  "profiles"
  "articles"
  "articles_liked"
  "articles_saved"
  "habits"
  "habits_days"
  "habits_tracking"
  "user_habit_calendar_view"
  "user_articles_view"
  "user_habit_tracking_view"
  "user_habits_view"
  "ranked_articles_view"
  "habits_daily_trigger"
  "user_habit_categories_view"
)

# Plantilla de meta para activar colecciones (basada en tu ejemplo de "articles")
META_TEMPLATE='{
  "collection": "%s",
  "icon": null,
  "note": null,
  "display_template": null,
  "hidden": false,
  "singleton": false,
  "translations": null,
  "archive_field": null,
  "archive_app_filter": true,
  "archive_value": null,
  "unarchive_value": null,
  "sort_field": null,
  "accountability": "all",
  "color": null,
  "item_duplication_fields": null,
  "sort": null,
  "group": null,
  "collapse": "open",
  "preview_url": null,
  "versioning": false
}'

# 4) Activar todas las colecciones en el Data Model
echo "$(date '+%Y-%m-%d %H:%M:%S') Activando colecciones en el Data Model..."

for COLLECTION in "${COLLECTIONS[@]}"; do
  echo "$(date '+%Y-%m-%d %H:%M:%S') Activando colección: $COLLECTION"
  
  # Crear el objeto meta para esta colección
  META_JSON=$(printf "$META_TEMPLATE" "$COLLECTION")
  
  # Enviar solicitud para activar la colección
  ACTIVATE_RESP=$(curl -s -X PATCH "http://localhost:8055/collections/$COLLECTION" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "meta": '"$META_JSON"'
    }')
  
  # Verificar si hay errores en la respuesta
  if echo "$ACTIVATE_RESP" | grep -q '"errors"'; then
    echo "$(date '+%Y-%m-%d %H:%M:%S') ADVERTENCIA: Error al activar colección $COLLECTION. Respuesta:"
    echo "$ACTIVATE_RESP"
  else
    echo "$(date '+%Y-%m-%d %H:%M:%S') Colección $COLLECTION activada correctamente."
  fi
  
  # Esperar un momento para asegurar que la colección se haya activado
  sleep 1
done

echo "$(date '+%Y-%m-%d %H:%M:%S') Configuración de colecciones completada."

# 5) Salir
echo "$(date '+%Y-%m-%d %H:%M:%S') Script completado."
exit 0