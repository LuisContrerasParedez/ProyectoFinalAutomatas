import { Button, VStack, HStack, Text, Box } from "@chakra-ui/react";

export function ProcessList({
  ids, idSel, onSelect, onCreate,
}: { ids: string[]; idSel: string | null; onSelect: (id: string)=>void; onCreate: ()=>void }) {
  return (
    <VStack align="stretch" spacing={2}>
      <HStack justify="space-between">
        <Text fontWeight="bold">Procesos</Text>
        <Button size="sm" colorScheme="teal" onClick={onCreate}>Nuevo</Button>
      </HStack>
      {ids.map(id => (
        <Box key={id}
          p={2}
          rounded="md"
          borderWidth="1px"
          bg={idSel === id ? "teal.500" : "gray.700"}
          color={idSel === id ? "white" : "gray.100"}
          cursor="pointer"
          onClick={()=>onSelect(id)}
          fontFamily="mono"
          fontSize="xs"
        >
          {id}
        </Box>
      ))}
      {!ids.length && <Text fontSize="sm" color="gray.400">No hay procesos</Text>}
    </VStack>
  );
}
