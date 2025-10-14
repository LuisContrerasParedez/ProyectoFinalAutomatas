import { Button, VStack, HStack, NumberInput, NumberInputField, Select } from "@chakra-ui/react";
import { useState } from "react";
import type { MateriaPrimaReq, Tipo, Calidad } from "../lib/api";

const TIPOS: Tipo[] = ["PINO","CAOBA","ROBLE","MDF","OTRO"];
const CALIDADES: Calidad[] = ["ALTA","MEDIA","BAJA"];

export function MateriaPrimaForm({ onSave, isDisabled }:{
  onSave: (mp: MateriaPrimaReq)=>void, isDisabled?: boolean
}) {
  const [tipo, setTipo] = useState<Tipo>("PINO");
  const [calidad, setCalidad] = useState<Calidad>("ALTA");
  const [humedadPorc, setHumedad] = useState(8);
  const [cantidad, setCantidad] = useState(10);

  return (
    <VStack align="stretch" spacing={2}>
      <HStack>
        <Select value={tipo} onChange={e=>setTipo(e.target.value as Tipo)} isDisabled={isDisabled}>
          {TIPOS.map(t=><option key={t} value={t}>{t}</option>)}
        </Select>
        <Select value={calidad} onChange={e=>setCalidad(e.target.value as Calidad)} isDisabled={isDisabled}>
          {CALIDADES.map(c=><option key={c} value={c}>{c}</option>)}
        </Select>
      </HStack>
      <HStack>
        <NumberInput min={0} max={100} value={humedadPorc} onChange={(_,v)=>setHumedad(v)} isDisabled={isDisabled}>
          <NumberInputField placeholder="Humedad %" />
        </NumberInput>
        <NumberInput min={1} value={cantidad} onChange={(_,v)=>setCantidad(v)} isDisabled={isDisabled}>
          <NumberInputField placeholder="Cantidad" />
        </NumberInput>
      </HStack>
      <Button colorScheme="teal" onClick={()=>onSave({tipo, calidad, humedadPorc, cantidad})} isDisabled={isDisabled}>
        Guardar Materia Prima
      </Button>
    </VStack>
  );
}
