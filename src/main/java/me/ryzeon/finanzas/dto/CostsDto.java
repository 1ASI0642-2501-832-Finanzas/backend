package me.ryzeon.finanzas.dto;

import me.ryzeon.finanzas.entity.Costs;

import java.math.BigDecimal;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 05:09
 */
public record CostsDto(
        String reason,
        BigDecimal value,
        String type
) {
    public static Costs toEntity(CostsDto costsDto) {
        Costs costs = new Costs();
        costs.setReason(costsDto.reason());
        costs.setValue(costsDto.value());
        costs.setType(costsDto.type());
        return costs;
    }
}
