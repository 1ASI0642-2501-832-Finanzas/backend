package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record CreateWalletRequest(
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date discountDate,
        String userIdentifier
) {
}
