import { Badge, Card, CardBody, Heading, Text, Stack } from '@chakra-ui/react'
import type { EstadoRes } from '../types'

export default function EstadoPanel({ estado }: { estado: EstadoRes | null }) {
  return (
    <Card>
      <CardBody>
        <Stack spacing={2}>
          <Heading size="md">Estado actual</Heading>
          <Text><Badge>{estado?.estadoActual ?? '...'}</Badge></Text>
          <Text fontSize="sm" color="gray.500">
            {estado?.materiaPrima ?? '(sin materia prima)'}
          </Text>
          {estado?.terminal && <Badge colorScheme="red">Estado terminal</Badge>}
        </Stack>
      </CardBody>
    </Card>
  )
}
