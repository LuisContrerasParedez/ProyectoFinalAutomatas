import { Card, CardHeader, CardBody, Heading, Button, ButtonGroup, useToast, Stack } from '@chakra-ui/react'
import { api } from '../api'
import type { Decision, EstadoRes } from '../types'

export default function ActionPad({
  estado,
  onChange,
}: {
  estado: EstadoRes | null
  onChange: () => void
}) {
  const toast = useToast()
  const enInspeccion = (estado?.estadoActual || '').toUpperCase().includes('INSPECCION')

  const setDecision = async (d?: Decision) => {
    try {
      await api.decidir(d)
      toast({ title: `Decisión: ${d ?? 'AUTO'}`, status: 'info' })
      onChange()
    } catch (e: any) {
      toast({ title: 'No se pudo registrar la decisión', description: e?.response?.data?.message ?? String(e), status: 'error' })
    }
  }

  const avanzar = async () => {
    try {
      const e = await api.avanzar()
      toast({ title: `Nuevo estado: ${e.estadoActual}`, status: 'success' })
      onChange()
    } catch (e: any) {
      const msg = e?.response?.data?.message ?? 'Transición inválida'
      toast({ title: 'No se pudo avanzar', description: msg, status: 'error', duration: 6000, isClosable: true })
    }
  }

  const reiniciar = async () => {
    await api.reiniciar()
    toast({ title: 'Proceso reiniciado', status: 'info' })
    onChange()
  }

  return (
    <Card>
      <CardHeader pb={2}><Heading size="sm">Acciones</Heading></CardHeader>
      <CardBody>
        <Stack gap={4}>
          {enInspeccion && (
            <>
              <Heading size="xs" color="gray.500">Decisión en Inspección</Heading>
              <ButtonGroup size="sm" variant="outline" isAttached>
                <Button onClick={() => setDecision('APTA')}>APTA</Button>
                <Button onClick={() => setDecision('RECHAZADA')}>RECHAZADA</Button>
                <Button onClick={() => setDecision('REPROCESO')}>REPROCESO</Button>
                <Button onClick={() => setDecision(undefined)}>AUTO</Button>
              </ButtonGroup>
            </>
          )}

          <Button colorScheme="teal" onClick={avanzar} isDisabled={!estado || estado.terminal}>Avanzar</Button>
          <Button variant="outline" onClick={reiniciar}>Reiniciar</Button>
        </Stack>
      </CardBody>
    </Card>
  )
}
