import { Box, Container, Flex, Heading } from '@chakra-ui/react'
import type { ReactNode } from 'react'

export default function Shell({ sidebar, children, actions }: { sidebar: ReactNode; children: ReactNode; actions: ReactNode }) {
  return (
    <Flex h="100vh" bg="wood.50">
      <Box w={{ base: '0', md: '310px' }} display={{ base: 'none', md: 'block' }} bg="white" borderRight="1px solid" borderColor="gray.100" p={6}>
        <Heading size="md" mb={6}>Flujo de Producción</Heading>
        {sidebar}
      </Box>
      <Flex direction="column" flex="1">
        <Box as="header" px={6} py={4} bg="white" borderBottom="1px solid" borderColor="gray.100">
          <Container maxW="7xl">
            <Heading size="lg">Fábrica – Control de Producción</Heading>
          </Container>
        </Box>
        <Box as="main" flex="1" overflow="auto">
          <Container maxW="7xl" py={6}>{children}</Container>
        </Box>
        <Box as="footer" position="sticky" bottom={0} bg="white" borderTop="1px solid" borderColor="gray.100" py={3}>
          <Container maxW="7xl">{actions}</Container>
        </Box>
      </Flex>
    </Flex>
  )
}
