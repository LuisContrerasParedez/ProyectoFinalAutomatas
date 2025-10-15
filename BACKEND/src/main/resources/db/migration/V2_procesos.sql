BEGIN;

-- 0) Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) Tabla procesos (match a tu entidad Proceso)
CREATE TABLE IF NOT EXISTS procesos (
  id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at    timestamptz NOT NULL DEFAULT now(),
  estado_actual text       NOT NULL,
  terminal      boolean    NOT NULL DEFAULT false,
  mp_tipo       text,
  mp_calidad    text,
  mp_humedad    double precision,
  mp_cantidad   integer
);

-- 2) Tabla destino correcta para eventos (match a tu entidad ProcesoEvent)
--    - Nombre: proceso_event (singular)
--    - id: BIGSERIAL (Long / IDENTITY)
--    - proceso_id: uuid con FK a procesos(id)
--    - estado_origen / estado_destino: PERMITEN NULL
CREATE TABLE IF NOT EXISTS proceso_event (
  id             BIGSERIAL PRIMARY KEY,
  created_at     timestamptz NOT NULL DEFAULT now(),
  proceso_id     uuid        NOT NULL REFERENCES procesos(id) ON DELETE CASCADE,
  estado_origen  text,
  estado_destino text,
  mp_tipo        text,
  mp_calidad     text,
  mp_humedad     double precision,
  mp_cantidad    integer
);

-- 2.1) Si por alguna razón el tipo de proceso_id no fuera uuid, cámbialo con USING
DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_name = 'proceso_event'
      AND column_name = 'proceso_id'
      AND udt_name <> 'uuid'
  ) THEN
    EXECUTE 'ALTER TABLE proceso_event
             ALTER COLUMN proceso_id TYPE uuid
             USING proceso_id::uuid';
  END IF;
END $$;

-- 2.2) Asegura que estado_origen/destino permitan NULL (primer evento no tiene origen)
ALTER TABLE proceso_event
  ALTER COLUMN estado_origen  DROP NOT NULL,
  ALTER COLUMN estado_destino DROP NOT NULL;

-- 3) MIGRACIÓN desde tabla vieja, si existe: proceso_events (plural, id uuid)
DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_name = 'proceso_events'
  ) THEN
    -- Antes de copiar, valida que proceso_id sea casteable a uuid.
    -- Si hubiera filas inválidas, elimínalas o corrígelas.
    -- Aquí copiamos y casteamos.
    EXECUTE $mig$
      INSERT INTO proceso_event (
        created_at, proceso_id, estado_origen, estado_destino,
        mp_tipo, mp_calidad, mp_humedad, mp_cantidad
      )
      SELECT
        COALESCE(created_at, now()),
        proceso_id::uuid,
        NULLIF(estado_origen,  ''),  -- por si venía con '', lo convertimos a NULL
        NULLIF(estado_destino, ''),
        mp_tipo, mp_calidad, mp_humedad, mp_cantidad
      FROM proceso_events
    $mig$;

    -- Deja respaldo renombrando la tabla vieja
    ALTER TABLE proceso_events RENAME TO proceso_events_backup;
  END IF;
END $$;

-- 4) Índices para acelerar tus consultas
--    Usas findByProcesoIdOrderByIdAsc => índice por (proceso_id, id)
CREATE INDEX IF NOT EXISTS idx_proceso_event_proceso_id_id
  ON proceso_event (proceso_id, id);

--    Opcional: si ordenas/filtras por created_at también:
CREATE INDEX IF NOT EXISTS idx_proceso_event_created_at
  ON proceso_event (created_at);

COMMIT;
