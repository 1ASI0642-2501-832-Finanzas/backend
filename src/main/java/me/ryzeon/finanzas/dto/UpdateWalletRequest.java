package me.ryzeon.finanzas.dto;

import java.util.Date;

public record UpdateWalletRequest(
        String name,
        String description,
        Date discountDate
) {
}
