import { VStack, HStack, Box, Text, Icon, Circle } from '@chakra-ui/react'
import { CheckIcon } from '@chakra-ui/icons'

const ORDER = [
  'Recepcion de Materia Prima',
  'Inspeccion de Calidad',
  'Corte y Dimensionado',
  'Secado y Tratamiento',
  'Almacenado en Inventario',
  'Distribuido a Produccion',
]

export default function VerticalStepper({ actual }: { actual?: string }) {
  const index = ORDER.findIndex(s => (actual ?? '').toLowerCase().includes(s.split(' ')[0].toLowerCase()))
  return (
    <VStack align="stretch" spacing={5}>
      {ORDER.map((s, i) => {
        const done = index > i
        const current = index === i
        return (
          <HStack key={s} align="start">
            <Circle size="28px" bg={done ? 'green.400' : current ? 'teal.400' : 'gray.200'} color="white">
              {done ? <Icon as={CheckIcon} /> : <Box w="10px" h="10px" bg="whiteAlpha.800" borderRadius="full" />}
            </Circle>
            <Box>
              <Text fontWeight={current ? 'bold' : 'medium'}>{s}</Text>
              <Text fontSize="xs" color="gray.500">
                {i + 1} / {ORDER.length}
              </Text>
            </Box>
          </HStack>
        )
      })}
    </VStack>
  )
}
