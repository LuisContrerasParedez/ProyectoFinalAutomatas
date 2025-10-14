import { HStack, Button, Tooltip } from "@chakra-ui/react";
import type { Decision } from "../lib/api";

export function DecisionBar({ onPick, disabled }:{
  onPick: (d: Decision)=>void; disabled?: boolean;
}) {
  return (
    <HStack>
      <Tooltip label="Permite continuar a Corte y Dimensionado">
        <Button size="sm" colorScheme="green" onClick={()=>onPick("APTA")} isDisabled={disabled}>APTA</Button>
      </Tooltip>
      <Tooltip label="Termina el proceso en Rechazado">
        <Button size="sm" colorScheme="red" onClick={()=>onPick("RECHAZADA")} isDisabled={disabled}>RECHAZADA</Button>
      </Tooltip>
    </HStack>
  );
}
