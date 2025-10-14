export type Tipo = 'PINO' | 'CAOBA' | 'ROBLE' | 'MDF' | 'OTRO'
export type Calidad = 'ALTA' | 'MEDIA' | 'BAJA'
export type Decision = 'APTA' | 'RECHAZADA' | 'REPROCESO'

export interface MateriaPrimaReq {
  tipo: Tipo
  calidad: Calidad
  humedadPorc: number
  cantidad: number
}

export interface EstadoRes {
  estadoActual: string
  materiaPrima: string | null
  terminal: boolean
}

export interface ProcesoEvent {
  id: number
  proceso_id: string
  estado_origen: string
  estado_destino: string
  mp_tipo?: string
  mp_calidad?: string
  mp_humedad?: number
  mp_cantidad?: number
  created_at: string
}
