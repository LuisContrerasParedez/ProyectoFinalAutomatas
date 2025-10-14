import axios, { AxiosError } from "axios";

// === Base URL (usa tu .env.local) ===
const BASE_URL = import.meta.env.VITE_API || "http://localhost:8080/api";

export const api = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
  timeout: 15000,
});

// Interceptor para mostrar errores legibles
api.interceptors.response.use(
  (res) => res,
  (err: AxiosError) => {
    const status = err.response?.status;
    const data: any = err.response?.data;
    const serverMsg =
      (typeof data === "string" && data) ||
      data?.message ||
      data?.error ||
      err.message;

    const msg = `[API ${status ?? "ERR"}] ${serverMsg}`;
    return Promise.reject(new Error(msg));
  }
);

function toArray<T>(data: unknown): T[] {
  if (Array.isArray(data)) return data as T[];
  if (data == null) return [];
  if (typeof data === "object") {
    const v = (data as any).ids ?? Object.values(data as any);
    return Array.isArray(v) ? (v as T[]) : [];
  }
  return [];
}

// === Tipos DTO ===
export type Tipo = "PINO" | "CAOBA" | "ROBLE" | "MDF" | "OTRO";
export type Calidad = "ALTA" | "MEDIA" | "BAJA";
export type Decision = "APTA" | "RECHAZADA";

export interface MateriaPrimaReq {
  tipo: Tipo;
  calidad: Calidad;
  humedadPorc: number;
  cantidad: number;
}

export interface EstadoRes {
  estadoActual: string;
  materiaPrima: string | null;
  terminal: boolean;
}

export const procesosApi = {
  crear: async (): Promise<string> => {
    const { data } = await api.post<string>("/procesos");
    if (typeof data === "string") return data;
    const id = (data as any)?.id ?? (Array.isArray(data) ? data[0] : null);
    if (!id) throw new Error("Respuesta inesperada al crear proceso");
    return String(id);
  },

  listar: async (): Promise<string[]> => {
    const { data } = await api.get("/procesos");
    return toArray<string>(data);
  },

  estado: async (id: string): Promise<EstadoRes> => {
    const { data } = await api.get<EstadoRes>(`/procesos/${id}/estado`);
    return data;
  },

  historial: async (id: string): Promise<string[]> => {
    const { data } = await api.get<string[]>(`/procesos/${id}/historial`);
    return toArray<string>(data);
  },

  setMp: async (id: string, body: MateriaPrimaReq): Promise<void> => {
    await api.post(`/procesos/${id}/mp`, body);
  },

  decidir: async (id: string, decision: Decision): Promise<void> => {
    await api.post(`/procesos/${id}/decision`, { decision });
  },

  avanzar: async (id: string): Promise<string> => {
    const { data } = await api.post<string>(`/procesos/${id}/avanzar`);
    return typeof data === "string" ? data : "OK";
  },

  reiniciar: async (id: string): Promise<void> => {
    await api.post(`/procesos/${id}/reiniciar`);
  },
};
