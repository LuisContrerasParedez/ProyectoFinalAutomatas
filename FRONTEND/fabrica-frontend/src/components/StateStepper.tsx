import { VStack, HStack, Box, Text, Icon, Badge } from '@chakra-ui/react'
import { CheckCircleIcon, SmallCloseIcon, TimeIcon } from '@chakra-ui/icons'

const ORDER = [
  'Recepcion de Materia Prima',
  'Inspeccion de Calidad',
  'Corte y Dimensionado',
  'Secado y Tratamiento',
  'Almacenado en Inventario',
  'Distribuido a Produccion'
]

export default function StateStepper({ actual }: { actual?: string }) {
  const idx = ORDER.findIndex(s => (actual ?? '').toLowerCase().includes(s.split(' ')[0].toLowerCase()))
  return (
    <VStack align="stretch" spacing={3}>
      {ORDER.map((s, i) => {
        const done = idx > i
        const current = idx === i
        return (
          <HStack key={s} align="start">
            <Icon
              as={done ? CheckCircleIcon : current ? TimeIcon : SmallCloseIcon}
              color={done ? 'brand.500' : current ? 'brand.400' : 'gray.300'}
              boxSize={5}
              mt={1}
            />
            <Box>
              <Text fontWeight={current ? 'bold' : 'medium'}>{s}</Text>
              {current && <Badge colorScheme="brand">Actual</Badge>}
            </Box>
          </HStack>
        )
      })}
    </VStack>
  )
}
