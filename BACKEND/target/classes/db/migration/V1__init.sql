CREATE TABLE IF NOT EXISTS materia_prima (
  id BIGSERIAL PRIMARY KEY,
  tipo VARCHAR(20) NOT NULL,
  calidad VARCHAR(20) NOT NULL,
  humedad_porc DOUBLE PRECISION NOT NULL,
  cantidad INTEGER NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS proceso_event (
  id BIGSERIAL PRIMARY KEY,
  proceso_id VARCHAR(64) NOT NULL,
  estado_origen VARCHAR(80) NOT NULL,
  estado_destino VARCHAR(80) NOT NULL,
  mp_tipo VARCHAR(20),
  mp_calidad VARCHAR(20),
  mp_humedad DOUBLE PRECISION,
  mp_cantidad INTEGER,
  created_at TIMESTAMPTZ DEFAULT now()
);
