import { Button, VStack, HStack, Text, Box } from "@chakra-ui/react";

export function ProcessList({
  ids, idSel, onSelect, onCreate,
}: { ids: string[]; idSel: string | null; onSelect: (id: string) => void; onCreate: () => void }) {
  return (
    <VStack align="stretch" spacing={2}>
      <HStack justify="space-between">
        <Text fontWeight="bold">Procesos</Text>
        <Button size="sm" colorScheme="teal" onClick={onCreate}>Nuevo</Button>
      </HStack>
      {ids.map(id => (
        <Box
          key={id}
          p={2}
          rounded="full"
          borderWidth="1px"
          borderColor={idSel === id ? "accent.500" : "gray.200"}
          bg={idSel === id ? "accent.500" : "white"}
          color={idSel === id ? "white" : "gray.800"}
          cursor="pointer"
          onClick={() => onSelect(id)}
          fontFamily="mono"
          fontSize="xs"
          >
          { id }
        </Box>
  ))
}
{ !ids.length && <Text fontSize="sm" color="gray.400">No hay procesos</Text> }
    </VStack >
  );
}
