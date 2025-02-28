package me.ryzeon.finanzas.dto;

import java.util.Date;

public record CreateWalletRequest(
        String name,
        String description,
        Date discountDate,
        String userIdentifier
) {
}
