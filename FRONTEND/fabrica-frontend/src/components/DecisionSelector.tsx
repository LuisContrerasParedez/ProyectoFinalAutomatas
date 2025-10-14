import { Button, ButtonGroup, Card, CardBody, Heading, Stack, useToast } from '@chakra-ui/react'
import { api } from '../api'
import type { Decision } from '../types'

export default function DecisionSelector({ disabled, onChange }: { disabled?: boolean; onChange: () => void }) {
  const toast = useToast()
  const set = async (d?: Decision) => {
    await api.decidir(d)
    toast({ title: `Decisión: ${d ?? 'AUTO'}`, status: 'info' })
    onChange()
  }
  return (
    <Card opacity={disabled ? 0.6 : 1}>
      <CardBody>
        <Stack gap={3}>
          <Heading size="sm">Decisión en Inspección</Heading>
          <ButtonGroup isDisabled={disabled} size="sm" variant="outline">
            <Button onClick={() => set('APTA')}>APTA</Button>
            <Button onClick={() => set('RECHAZADA')}>RECHAZADA</Button>
            <Button onClick={() => set('REPROCESO')}>REPROCESO</Button>
            <Button onClick={() => set(undefined)}>AUTO</Button>
          </ButtonGroup>
        </Stack>
      </CardBody>
    </Card>
  )
}
