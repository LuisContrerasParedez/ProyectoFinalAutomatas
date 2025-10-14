import { Alert, AlertIcon, AlertTitle, AlertDescription, Badge } from '@chakra-ui/react'

export default function StatusBanner({ estado, terminal, materiaPrima }: { estado?: string; terminal?: boolean; materiaPrima?: string | null }) {
  const isInspect = (estado ?? '').toUpperCase().includes('INSPECCION')
  const color = terminal ? 'red' : isInspect ? 'yellow' : 'green'
  return (
    <Alert status={terminal ? 'error' : isInspect ? 'warning' : 'success'} borderRadius="xl">
      <AlertIcon />
      <AlertTitle mr={2}>{(estado ?? '...').toUpperCase()}</AlertTitle>
      {terminal && <Badge ml={2} colorScheme="red">Terminal</Badge>}
      {!terminal && isInspect && <Badge ml={2} colorScheme="yellow">Decidir</Badge>}
      <AlertDescription ml={4} noOfLines={2}>
        {materiaPrima ?? '(sin materia prima)'}
      </AlertDescription>
    </Alert>
  )
}
