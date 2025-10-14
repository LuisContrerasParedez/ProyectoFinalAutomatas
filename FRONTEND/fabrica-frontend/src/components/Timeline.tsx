import { useEffect, useState } from 'react'
import { Card, CardHeader, CardBody, Heading, VStack, HStack, Text, Badge, Divider } from '@chakra-ui/react'
import { api } from '../api'
import type { ProcesoEvent } from '../types'

export default function Timeline({ refreshKey }: { refreshKey: number }) {
  const [rows, setRows] = useState<ProcesoEvent[]>([])
  useEffect(() => { api.historial().then(setRows) }, [refreshKey])

  return (
    <Card>
      <CardHeader pb={2}><Heading size="sm">Historial</Heading></CardHeader>
      <CardBody>
        <VStack align="stretch" spacing={3}>
          {rows.map((r, idx) => (
            <VStack key={r.id ?? idx} align="stretch" spacing={1}>
              <HStack>
                <Text fontSize="sm" color="gray.500">{new Date(r.created_at).toLocaleString()}</Text>
                <Badge ml={2}>{r.proceso_id}</Badge>
              </HStack>
              <HStack>
                <Badge colorScheme="gray">{r.estado_origen}</Badge>
                <Text fontSize="sm">→</Text>
                <Badge colorScheme="teal">{r.estado_destino}</Badge>
                {r.mp_tipo && (
                  <Badge ml={2} colorScheme="purple">
                    {r.mp_tipo}/{r.mp_calidad} H:{r.mp_humedad} C:{r.mp_cantidad}
                  </Badge>
                )}
              </HStack>
              <Divider />
            </VStack>
          ))}
          {rows.length === 0 && <Text fontSize="sm" color="gray.500">(sin eventos)</Text>}
        </VStack>
      </CardBody>
    </Card>
  )
}
