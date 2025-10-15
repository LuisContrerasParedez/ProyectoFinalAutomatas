BEGIN;
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name='proceso_event' AND column_name='id' AND udt_name='uuid'
  ) THEN
    ALTER TABLE proceso_event DROP CONSTRAINT proceso_event_pkey;
    ALTER TABLE proceso_event ADD COLUMN id_bigserial BIGSERIAL;
    ALTER TABLE proceso_event ADD PRIMARY KEY (id_bigserial);
    ALTER TABLE proceso_event RENAME COLUMN id TO id_uuid;
    ALTER TABLE proceso_event RENAME COLUMN id_bigserial TO id;
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name='proceso_event' AND column_name='proceso_id' AND udt_name <> 'uuid'
  ) THEN
    ALTER TABLE proceso_event
      ALTER COLUMN proceso_id TYPE uuid USING proceso_id::uuid;
  END IF;
END $$;

ALTER TABLE proceso_event
  ALTER COLUMN estado_origen  DROP NOT NULL,
  ALTER COLUMN estado_destino DROP NOT NULL;

CREATE INDEX IF NOT EXISTS idx_proceso_event_proceso_id_id
  ON proceso_event (proceso_id, id);

COMMIT;
