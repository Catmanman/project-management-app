# DB init

Place your schema and seed files here. Any `*.sql` or `*.sh` in this folder will run **once** on first boot.

Examples:
- `001_schema.sql` — create tables
- `002_seed.sql` — insert initial rows

To re-run seeds, wipe the named volume: `docker compose down -v` then `docker compose up --build`.
