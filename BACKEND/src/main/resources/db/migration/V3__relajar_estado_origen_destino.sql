BEGIN;

ALTER TABLE proceso_event
  ALTER COLUMN estado_origen  DROP NOT NULL,
  ALTER COLUMN estado_destino DROP NOT NULL;

-- Asegura tipo correcto, por si en otro entorno llega mal
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

COMMIT;
