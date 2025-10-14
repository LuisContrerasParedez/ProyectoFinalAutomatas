import { useEffect, useState } from 'react'
import { Table, Thead, Tbody, Tr, Th, Td, Card, CardBody } from '@chakra-ui/react'
import { api } from '../api'
import type { ProcesoEvent } from '../types'

export default function HistorialTable({ refreshKey }: { refreshKey: number }) {
  const [rows, setRows] = useState<ProcesoEvent[]>([])
  useEffect(() => { api.historial().then(setRows) }, [refreshKey])

  return (
    <Card>
      <CardBody>
        <Table size="sm">
          <Thead>
            <Tr>
              <Th>Fecha</Th><Th>Origen</Th><Th>Destino</Th><Th>MP</Th>
            </Tr>
          </Thead>
          <Tbody>
            {rows.map(r => (
              <Tr key={r.id}>
                <Td>{new Date(r.created_at).toLocaleString()}</Td>
                <Td>{r.estado_origen}</Td>
                <Td>{r.estado_destino}</Td>
                <Td>{r.mp_tipo ? `${r.mp_tipo}/${r.mp_calidad} H:${r.mp_humedad} C:${r.mp_cantidad}` : '-'}</Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </CardBody>
    </Card>
  )
}
