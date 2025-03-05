package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record UpdateWalletRequest(
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Discount date in the format dd-MM-yyyy", example = "28-02-2025")
        Date discountDate
) {
}
