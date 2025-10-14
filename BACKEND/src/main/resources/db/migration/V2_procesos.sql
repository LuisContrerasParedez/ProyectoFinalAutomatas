-- Tabla de procesos
create table if not exists procesos (
  id uuid primary key default gen_random_uuid(),
  created_at timestamptz not null default now(),
  estado_actual text not null,
  terminal boolean not null default false,
  mp_tipo text,
  mp_calidad text,
  mp_humedad double precision,
  mp_cantidad integer
);

-- Si ya tienes la tabla de eventos, sólo nos aseguramos del FK.
-- Si no la tienes, créala así (ajusta a tu naming si es distinto):

create table if not exists proceso_events (
  id uuid primary key default gen_random_uuid(),
  created_at timestamptz not null default now(),
  proceso_id uuid not null references procesos(id) on delete cascade,
  estado_origen text not null,
  estado_destino text not null,
  mp_tipo text,
  mp_calidad text,
  mp_humedad double precision,
  mp_cantidad integer
);

create index if not exists idx_events_proceso on proceso_events(proceso_id, created_at);
