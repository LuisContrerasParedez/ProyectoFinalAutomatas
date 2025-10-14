package edu.gt.miumg.fabrica.api.dto;

import edu.gt.miumg.fabrica.MateriaPrima;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MateriaPrimaReq(
        @NotNull MateriaPrima.Tipo tipo,
        @NotNull MateriaPrima.Calidad calidad,
        @DecimalMin("0") @DecimalMax("100") double humedadPorc,
        @Min(1) int cantidad
) {}
