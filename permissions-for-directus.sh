#!/bin/sh

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
  "directus_users"
  "directus_roles"
)

# ID del rol público
PUBLIC_ROLE="00000000-0000-0000-0000-000000000001"

# 1. Crear la política "Access"
echo "$(date '+%Y-%m-%d %H:%M:%S') Creando política 'Access'..."

POLICY_RESP=$(curl -s -X POST http://localhost:8055/policies \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Access",
    "icon": "security",
    "description": null,
    "ip_access": null,
    "enforce_tfa": false,
    "admin_access": false,
    "app_access": true,
    "permissions": [],
    "roles": ["'"$PUBLIC_ROLE"'"]
  }')

# Extraer el ID de la política creada
POLICY_ID=$(echo "$POLICY_RESP" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

if [ -z "$POLICY_ID" ]; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') ERROR: No se pudo crear la política. Respuesta:"
  echo "$POLICY_RESP"
  exit 1
fi

echo "$(date '+%Y-%m-%d %H:%M:%S') Política creada con ID: $POLICY_ID"

# Array para almacenar los IDs de los permisos creados
PERMISSION_IDS=()

# 2. Crear permisos para cada colección y acción
ACTIONS=("create" "read" "update" "delete" "share")

for COLLECTION in "${COLLECTIONS[@]}"; do
  echo "$(date '+%Y-%m-%d %H:%M:%S') Creando permisos para colección: $COLLECTION"
  
  for ACTION in "${ACTIONS[@]}"; do
    echo "$(date '+%Y-%m-%d %H:%M:%S') - Creando permiso de $ACTION"
    
    PERMISSION_RESP=$(curl -s -X POST http://localhost:8055/permissions \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" \
      -d '{
        "collection": "'"$COLLECTION"'",
        "action": "'"$ACTION"'",
        "fields": ["*"],
        "policy": "'"$POLICY_ID"'"
      }')
    
    # Extraer el ID del permiso creado
    PERMISSION_ID=$(echo "$PERMISSION_RESP" | grep -o '"id":[0-9]*' | cut -d':' -f2)
    
    if [ -n "$PERMISSION_ID" ]; then
      PERMISSION_IDS+=("$PERMISSION_ID")
      echo "$(date '+%Y-%m-%d %H:%M:%S')   Permiso creado con ID: $PERMISSION_ID"
    else
      echo "$(date '+%Y-%m-%d %H:%M:%S')   ADVERTENCIA: No se pudo crear permiso. Respuesta:"
      echo "$PERMISSION_RESP"
    fi
    
    sleep 0.5
  done
done

# 3. Actualizar la política con los IDs de los permisos creados
echo "$(date '+%Y-%m-%d %H:%M:%S') Actualizando política con los permisos creados..."

# Convertir el array de IDs a formato JSON
PERMISSIONS_JSON=$(printf '%s\n' "${PERMISSION_IDS[@]}" | jq -R . | jq -s .)

UPDATE_POLICY_RESP=$(curl -s -X PATCH "http://localhost:8055/policies/$POLICY_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "permissions": '"$PERMISSIONS_JSON"'
  }')

if echo "$UPDATE_POLICY_RESP" | grep -q '"errors"'; then
  echo "$(date '+%Y-%m-%d %H:%M:%S') ERROR: No se pudo actualizar la política. Respuesta:"
  echo "$UPDATE_POLICY_RESP"
else
  echo "$(date '+%Y-%m-%d %H:%M:%S') Política actualizada correctamente con todos los permisos"
fi

echo "$(date '+%Y-%m-%d %H:%M:%S') Script completado."
exit 0