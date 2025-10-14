import { Box, Grid, GridItem, Heading, VStack, HStack, Text, Button, useToast, Spinner } from "@chakra-ui/react";
import { useProceso } from "./store/useProceso";
import { ProcessList } from "./components/ProcessList";
import { MateriaPrimaForm } from "./components/MateriaPrimaForm";
import { DecisionBar } from "./components/DecisionBar";

export default function App() {
  const toast = useToast();
  const { ids, idSel, setIdSel, estado, historial, loading, actions } = useProceso();

  const esInspeccion = estado?.estadoActual?.toUpperCase().includes("INSPECCION");
  const esTerminal   = !!estado?.terminal;

  return (
    <Grid templateColumns="320px 1fr" gap={6} p={6}>
      <GridItem>
        <ProcessList
          ids={ids}
          idSel={idSel}
          onSelect={setIdSel}
          onCreate={async ()=>{
            await actions.crear();
            toast({ status:"success", title:"Proceso creado" });
          }}
        />
      </GridItem>

      <GridItem>
        <VStack align="stretch" spacing={6}>
          <Box p={4} rounded="lg" bg="gray.800" color="gray.100" borderWidth="1px">
            <HStack justify="space-between">
              <Heading size="md">Proceso actual</Heading>
              {loading && <Spinner size="sm" />}
            </HStack>
            <Text mt={2} fontWeight="bold">{estado?.estadoActual ?? "—"}</Text>
            {estado?.materiaPrima && <Text mt={1} fontSize="sm" color="gray.300">{estado.materiaPrima}</Text>}
            {esTerminal && <Text mt={2} color="pink.300" fontWeight="bold">TERMINAL</Text>}
          </Box>

          <Box p={4} rounded="lg" bg="gray.800" borderWidth="1px">
            <Heading size="sm" mb={3}>Operaciones</Heading>
            <HStack>
              <Button colorScheme="teal" onClick={async ()=>{
                try { await actions.avanzar(); toast({status:"success", title:"Avanzó"}); }
                catch (e:any) { toast({status:"error", title:"No se pudo avanzar", description:e.message}); }
              }} isDisabled={!idSel || esTerminal}>
                Avanzar
              </Button>
              <Button variant="outline" onClick={async ()=>{
                await actions.reiniciar(); toast({status:"info", title:"Reiniciado"});
              }} isDisabled={!idSel}>
                Reiniciar
              </Button>

              <DecisionBar
                onPick={async (d)=>{
                  try { await actions.decidir(d); toast({status:"success", title:`Decisión: ${d}`}); }
                  catch (e:any) { toast({status:"error", title:"Error al decidir", description:e.message}); }
                }}
                disabled={!idSel || !esInspeccion || esTerminal}
              />
            </HStack>
          </Box>

          <Box p={4} rounded="lg" bg="gray.800" borderWidth="1px">
            <Heading size="sm" mb={3}>Materia Prima</Heading>
            <MateriaPrimaForm
              isDisabled={!idSel || esTerminal}
              onSave={async (mp)=>{
                await actions.setMp(mp);
                toast({ status:"success", title:"Materia prima guardada" });
              }}
            />
          </Box>

          <Box p={4} rounded="lg" bg="gray.800" borderWidth="1px" maxH="340px" overflowY="auto">
            <Heading size="sm" mb={3}>Historial</Heading>
            <VStack align="stretch" spacing={1}>
              {historial.map((line, i)=>(
                <Text key={i} fontFamily="mono" fontSize="sm" color="gray.200">{line}</Text>
              ))}
              {!historial.length && <Text color="gray.500" fontSize="sm">Sin eventos</Text>}
            </VStack>
          </Box>
        </VStack>
      </GridItem>
    </Grid>
  );
}
