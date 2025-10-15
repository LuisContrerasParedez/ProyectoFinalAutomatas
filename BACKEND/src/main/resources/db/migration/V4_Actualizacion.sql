-- procesos
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

-- eventos (nombre singular para empatar con la entidad)
create table if not exists proceso_event (
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

create index if not exists idx_event_proceso on proceso_event(proceso_id, created_at);
