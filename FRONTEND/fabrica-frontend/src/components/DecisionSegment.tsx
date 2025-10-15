// components/DecisionSegment.tsx
import { ButtonGroup, Button, VisuallyHidden } from "@chakra-ui/react"
import type { Decision } from "../lib/api"
import { useState } from "react"

export function DecisionSegment({ onPick, disabled }:{
  onPick: (d: Decision)=>void; disabled?: boolean;
}) {
  const [sel, setSel] = useState<Decision | null>(null)
  const pick = (d:Decision)=>{ setSel(d); onPick(d) }

  return (
    <ButtonGroup isAttached variant="outline" size="sm" >
      <VisuallyHidden>Decidir calidad</VisuallyHidden>
      <Button
        onClick={()=>pick("APTA")}
        isDisabled={disabled}
        borderRightRadius="0"
        bg={sel==="APTA" ? "accent.500" : "white"}
        color={sel==="APTA" ? "white" : "gray.800"}
        _hover={{ bg: sel==="APTA" ? "accent.600" : "gray.100" }}
      >
        Apta
      </Button>
      <Button
        onClick={()=>pick("RECHAZADA")}
        isDisabled={disabled}
        borderLeftRadius="0"
        bg={sel==="RECHAZADA" ? "red.500" : "white"}
        color={sel==="RECHAZADA" ? "white" : "gray.800"}
        _hover={{ bg: sel==="RECHAZADA" ? "red.600" : "gray.100" }}
      >
        Rechazada
      </Button>
    </ButtonGroup>
  )
}
