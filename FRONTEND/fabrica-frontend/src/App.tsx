import {
  Box,
  Grid,
  GridItem,
  Heading,
  VStack,
  HStack,
  Text,
  Button,
  useToast,
  Spinner,
  Divider,
} from "@chakra-ui/react";
import { useProceso } from "./store/useProceso";
import { ProcessList } from "./components/ProcessList";
import { MateriaPrimaForm } from "./components/MateriaPrimaForm";
import { DecisionSegment } from "./components/DecisionSegment";
import { ProcessGraph } from "./components/ProcessGraph";
import { ProcessTimeline } from "./components/ProcessTimeline";

export default function App() {
  const toast = useToast();
  const { ids, idSel, setIdSel, estado, historial, loading, actions } = useProceso();

  const esInspeccion = estado?.estadoActual?.toUpperCase().includes("INSPECCION");
  const esTerminal = !!estado?.terminal;
  const puedeReprocesar = !!idSel && esInspeccion && !esTerminal;

  return (
    <Grid templateColumns="320px 1fr" gap={6} p={6} bg="white">
      <GridItem>
        <Box bg="white" borderWidth="1px" borderColor="gray.200" rounded="2xl" p={5}>
          <Heading size="md" mb={4}>
            Procesos
          </Heading>
          <ProcessList
            ids={ids}
            idSel={idSel}
            onSelect={setIdSel}
            onCreate={async () => {
              await actions.crear();
              toast({ status: "success", title: "Proceso creado" });
            }}
          />
        </Box>
      </GridItem>

      <GridItem>
        <VStack align="stretch" spacing={6}>
          <Box p={5} rounded="2xl" bg="white" borderWidth="1px" borderColor="gray.200">
            <HStack justify="space-between" mb={2}>
              <Heading size="md">Proceso actual</Heading>
              {loading && <Spinner size="sm" />}
            </HStack>
            <Text mt={1} fontWeight="semibold" fontSize="lg">
              {estado?.estadoActual ?? "—"}
            </Text>
            {estado?.materiaPrima && (
              <Text mt={1} fontSize="sm" color="gray.600">
                {estado.materiaPrima}
              </Text>
            )}
            {esTerminal && (
              <>
                <Divider my={3} />
                <Text color="red.500" fontWeight="semibold">
                  Estado terminal
                </Text>
              </>
            )}
          </Box>

          <ProcessGraph actual={estado?.estadoActual} />

          <Box p={5} rounded="2xl" bg="white" borderWidth="1px" borderColor="gray.200">
            <Heading size="sm" mb={3}>
              Operaciones
            </Heading>
            <HStack flexWrap="wrap" gap={3}>
              <Button
                onClick={async () => {
                  try {
                    await actions.avanzar();
                    toast({ status: "success", title: "Avanzó" });
                  } catch (e: any) {
                    toast({ status: "error", title: "No se pudo avanzar", description: e.message });
                  }
                }}
                isDisabled={!idSel || esTerminal || esInspeccion}
              >
                Avanzar
              </Button>

              <Button
                variant="outline"
                onClick={async () => {
                  await actions.reiniciar();
                  toast({ status: "info", title: "Reiniciado" });
                }}
                isDisabled={!idSel}
              >
                Reiniciar
              </Button>

              <DecisionSegment
                onPick={async (d) => {
                  try {
                    await actions.decidir(d);
                    toast({ status: "success", title: `Decisión: ${d}` });
                  } catch (e: any) {
                    toast({ status: "error", title: "Error al decidir", description: e.message });
                  }
                }}
                disabled={!idSel || !esInspeccion || esTerminal}
              />

              <Button
                variant="outline"
                onClick={async () => {
                  try {
                    await actions.reproceso();
                    toast({ status: "success", title: "Enviado a Reproceso" });
                  } catch (e: any) {
                    toast({ status: "error", title: "No se pudo reprocesar", description: e.message });
                  }
                }}
                isDisabled={!puedeReprocesar}
              >
                Reproceso
              </Button>
            </HStack>
          </Box>

          <Box p={5} rounded="2xl" bg="white" borderWidth="1px" borderColor="gray.200">
            <Heading size="sm" mb={3}>
              Materia Prima
            </Heading>
            <MateriaPrimaForm
              isDisabled={!idSel || esTerminal}
              onSave={async (mp) => {
                await actions.setMp(mp);
                toast({ status: "success", title: "Materia prima guardada" });
              }}
            />
          </Box>

          <ProcessTimeline items={historial} />
        </VStack>
      </GridItem>
    </Grid>
  );
}
