import { Box, Text, HStack, VStack } from "@chakra-ui/react";

type EstadoKey =
  | "RECEPCION_MP"
  | "INSPECCION_CALIDAD"
  | "CORTE_DIMENSIONADO"
  | "SECADO_TRATAMIENTO"
  | "ALMACENADO"
  | "DISTRIBUIDO_PRODUCCION"
  | "RECHAZADA"
  | "REPROCESO";

const LABELS: Record<EstadoKey, string> = {
  RECEPCION_MP: "Recepción",
  INSPECCION_CALIDAD: "Inspección",
  CORTE_DIMENSIONADO: "Corte",
  SECADO_TRATAMIENTO: "Secado",
  ALMACENADO: "Almacenado",
  DISTRIBUIDO_PRODUCCION: "Distribuido",
  RECHAZADA: "Rechazada",
  REPROCESO: "Reproceso",
};

// aristas válidas (para pintar “siguientes”)
const EDGES: Array<[EstadoKey, EstadoKey]> = [
  ["RECEPCION_MP", "INSPECCION_CALIDAD"],
  ["INSPECCION_CALIDAD", "CORTE_DIMENSIONADO"],
  ["INSPECCION_CALIDAD", "RECHAZADA"],
  ["INSPECCION_CALIDAD", "REPROCESO"],
  ["REPROCESO", "RECEPCION_MP"],
  ["CORTE_DIMENSIONADO", "SECADO_TRATAMIENTO"],
  ["SECADO_TRATAMIENTO", "ALMACENADO"],
  ["ALMACENADO", "DISTRIBUIDO_PRODUCCION"],
];

function nextOf(node: EstadoKey): EstadoKey[] {
  return EDGES.filter(([a]) => a === node).map(([, b]) => b);
}

export function ProcessGraph({ actual }: { actual?: string | null }) {
  const current = (actual?.toUpperCase() as EstadoKey) ?? null;
  const next = current ? nextOf(current) : [];

  const isCurrent = (k: EstadoKey) => current === k;
  const isNext = (k: EstadoKey) => next.includes(k);
  const isTerminal = (k: EstadoKey) => k === "DISTRIBUIDO_PRODUCCION" || k === "RECHAZADA";

  // layout manual (SVG) – posiciones (x,y)
  const POS: Record<EstadoKey, { x: number; y: number }> = {
    RECEPCION_MP: { x: 80, y: 80 },
    INSPECCION_CALIDAD: { x: 260, y: 80 },
    CORTE_DIMENSIONADO: { x: 440, y: 80 },
    SECADO_TRATAMIENTO: { x: 620, y: 80 },
    ALMACENADO: { x: 800, y: 80 },
    DISTRIBUIDO_PRODUCCION: { x: 980, y: 80 },

    RECHAZADA: { x: 440, y: 200 },     // rama abajo
    REPROCESO: { x: 260, y: 200 },     // bucle a recepción
  };

  const nodes: EstadoKey[] = Object.keys(POS) as EstadoKey[];

  return (
    <VStack align="stretch" spacing={3}>
      <HStack justify="space-between">
        <Text fontWeight="semibold">Flujo del proceso</Text>
        <Text fontSize="sm" color="gray.600">
          Actual: {current ? LABELS[current] : "—"}
        </Text>
      </HStack>

      <Box
        bg="white"
        borderWidth="1px"
        borderColor="gray.200"
        rounded="2xl"
        p={4}
        overflowX="auto"
      >
        <svg width={1100} height={280}>
          {/* edges */}
          {EDGES.map(([a, b], i) => {
            const pa = POS[a], pb = POS[b];
            const highlighted =
              (current && (a === current || b === current)) ||
              (current && b === current) ||
              (current && next.includes(b));

            const upDown = pa.y !== pb.y;
            const stroke = highlighted ? "#0A84FF" : "#D1D1D6";
            const strokeWidth = highlighted ? 3 : 2;

            // curva suave si vertical
            if (upDown) {
              const mx = (pa.x + pb.x) / 2;
              const my = (pa.y + pb.y) / 2;
              return (
                <path
                  key={i}
                  d={`M ${pa.x} ${pa.y} Q ${mx} ${pa.y} ${mx} ${my} T ${pb.x} ${pb.y}`}
                  fill="none"
                  stroke={stroke}
                  strokeWidth={strokeWidth}
                  markerEnd="url(#arrow)"
                  opacity={highlighted ? 1 : 0.7}
                />
              );
            }
            // línea recta
            return (
              <line
                key={i}
                x1={pa.x}
                y1={pa.y}
                x2={pb.x}
                y2={pb.y}
                stroke={stroke}
                strokeWidth={strokeWidth}
                markerEnd="url(#arrow)"
                opacity={highlighted ? 1 : 0.7}
              />
            );
          })}

          <defs>
            <marker id="arrow" markerWidth="10" markerHeight="10" refX="10" refY="3" orient="auto">
              <path d="M0,0 L0,6 L9,3 z" fill="#A0A0A3" />
            </marker>
          </defs>

          {/* nodes */}
          {nodes.map((k) => {
            const p = POS[k];
            const currentNode = isCurrent(k);
            const nextNode = isNext(k);
            const radius = 22;
            const stroke = currentNode ? "#0A84FF" : nextNode ? "#0A84FF" : "#D1D1D6";
            const fill = currentNode ? "#0A84FF" : nextNode ? "#E6F0FF" : "white";
            const textFill = currentNode ? "white" : "#1C1C1E";
            const dash = isTerminal(k) ? "4,4" : undefined;

            return (
              <g key={k}>
                <circle
                  cx={p.x}
                  cy={p.y}
                  r={radius}
                  fill={fill}
                  stroke={stroke}
                  strokeWidth={2.5}
                  strokeDasharray={dash}
                />
                <text x={p.x} y={p.y + 40} fontSize={12} textAnchor="middle" fill="#3A3A3C">
                  {LABELS[k]}
                </text>
                <text
                  x={p.x}
                  y={p.y + 2}
                  fontSize={10}
                  fontWeight={600}
                  textAnchor="middle"
                  fill={textFill}
                >
                  {k === "RECEPCION_MP" ? "REC" :
                   k === "INSPECCION_CALIDAD" ? "INSP" :
                   k === "CORTE_DIMENSIONADO" ? "CORTE" :
                   k === "SECADO_TRATAMIENTO" ? "SEC" :
                   k === "ALMACENADO" ? "ALM" :
                   k === "DISTRIBUIDO_PRODUCCION" ? "DIST" :
                   k === "RECHAZADA" ? "RECH" :
                   "REPRO"}
                </text>
              </g>
            );
          })}
        </svg>
      </Box>

      <HStack spacing={4}>
        <HStack>
          <Box boxSize="10px" bg="#0A84FF" borderRadius="full" />
          <Text fontSize="xs">Actual</Text>
        </HStack>
        <HStack>
          <Box boxSize="10px" bg="#E6F0FF" borderRadius="full" border="1px solid #0A84FF" />
          <Text fontSize="xs">Siguiente posible</Text>
        </HStack>
        <HStack>
          <Box boxSize="10px" borderRadius="full" border="2px dashed #D1D1D6" />
          <Text fontSize="xs">Estado terminal</Text>
        </HStack>
      </HStack>
    </VStack>
  );
}
