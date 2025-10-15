import { Box, VStack, HStack, Text, Circle } from "@chakra-ui/react";

export function ProcessTimeline({ items }: { items: string[] }) {
  return (
    <Box bg="white" borderWidth="1px" borderColor="gray.200" rounded="2xl" p={5} maxH="340px" overflowY="auto">
      <Text fontWeight="semibold" mb={3}>Línea de tiempo</Text>
      <VStack align="stretch" spacing={3}>
        {items.map((line, i) => {
          const isState = line.startsWith("ESTADO:");
          return (
            <HStack key={i} align="start" spacing={3}>
              <Circle size="10px" bg={isState ? "accent.500" : "gray.300"} />
              <Text fontFamily="mono" fontSize="sm" color="gray.800">{line}</Text>
            </HStack>
          );
        })}
        {!items.length && <Text color="gray.500" fontSize="sm">Sin eventos</Text>}
      </VStack>
    </Box>
  );
}
