package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import me.ryzeon.finanzas.entity.Wallet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:45
 */
public record WalletDto(
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Discount date in the format dd-MM-yyyy", example = "28-02-2025")
        Date discountDate,

        BigDecimal tcea
) {

    public WalletDto(Wallet wallet) {
        this(wallet.getId(), wallet.getName(), wallet.getDescription(), wallet.getDiscountDate(), wallet.getTcea());
    }
}

