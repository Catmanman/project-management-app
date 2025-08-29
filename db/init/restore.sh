#!/usr/bin/env sh
set -euo pipefail

DB="${POSTGRES_DB:-postgres}"
USER="${POSTGRES_USER:-postgres}"
JOBS="${RESTORE_JOBS:-4}"

echo "[restore] Restoring all custom dumps (*.dump|*.backup|*.dmp|*.pgdump) into DB '$DB' as '$USER'"

found=0
for dump in /docker-entrypoint-initdb.d/*.dump /docker-entrypoint-initdb.d/*.backup /docker-entrypoint-initdb.d/*.dmp /docker-entrypoint-initdb.d/*.pgdump; do
  [ -f "$dump" ] || continue
  found=1
  echo "[restore] -> $dump"
  pg_restore --verbose --no-owner --role="$USER" -U "$USER" -d "$DB" -j "$JOBS" "$dump"
done

if [ "$found" -eq 0 ]; then
  echo "[restore] No custom-format dumps found. Skipping."
fi

echo "[restore] All dump restores complete."
