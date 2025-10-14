import { useEffect, useState } from "react";
import { procesosApi, type EstadoRes, type MateriaPrimaReq, type Decision } from "../lib/api";

export function useProceso() {
  const [ids, setIds] = useState<string[]>([]);
  const [idSel, setIdSel] = useState<string | null>(null);
  const [estado, setEstado] = useState<EstadoRes | null>(null);
  const [historial, setHistorial] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  async function refresh(id = idSel) {
    if (!id) return;
    setLoading(true);
    try {
      const [e, h] = await Promise.all([procesosApi.estado(id), procesosApi.historial(id)]);
      setEstado(e);
      setHistorial(h);
    } finally {
      setLoading(false);
    }
  }

  async function cargarProcesos() {
    const data = await procesosApi.listar();
    setIds(data);
    if (!data.length) {
      const nuevo = await procesosApi.crear();
      setIds([nuevo]);
      setIdSel(nuevo);
    } else if (!idSel) {
      setIdSel(data[0]);
    }
  }

  async function crear() {
    const nuevo = await procesosApi.crear();
    setIds(prev => [nuevo, ...prev]);
    setIdSel(nuevo);
    await refresh(nuevo);
  }

  async function setMp(mp: MateriaPrimaReq) {
    if (!idSel) return;
    await procesosApi.setMp(idSel, mp);
    await refresh();
  }

  async function decidir(decision: Decision) {
    if (!idSel) return;
    await procesosApi.decidir(idSel, decision);
    await refresh();
  }

  async function avanzar() {
    if (!idSel) return;
    await procesosApi.avanzar(idSel);
    await refresh();
  }

  async function reiniciar() {
    if (!idSel) return;
    await procesosApi.reiniciar(idSel);
    await refresh();
  }

  useEffect(() => { cargarProcesos(); }, []);
  useEffect(() => { if (idSel) refresh(idSel); }, [idSel]);

  return {
    ids, idSel, setIdSel,
    estado, historial,
    loading,
    actions: { crear, setMp, decidir, avanzar, reiniciar, refresh }
  };
}
