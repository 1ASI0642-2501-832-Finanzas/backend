package me.ryzeon.finanzas.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 05:09
 */
public record CreateInvoiceRequest(
        String invoiceType,
        String financialInstitutionName,
        String number,
        String series,
        String issuerName,
        String issuerRuc,
        String currency,
        BigDecimal amount,
        BigDecimal igv,
        Date emissionDate,
        Date dueDate,
        Date discountDate,
        String terms,
        BigDecimal nominalRate,
        BigDecimal effectiveRate,
        List<CostsDto> initialCosts,
        List<CostsDto> finalCosts,
        String status,
        Long walletId
) {

    public BigDecimal calculateTcea() {
        if (nominalRate == null || amount == null || emissionDate == null || dueDate == null || discountDate == null) {
            throw new IllegalArgumentException("Nominal rate, amount, or dates cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        BigDecimal adjustedNominalRate = nominalRate.divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP);

        long daysBetweenEmissionAndDue = (dueDate.getTime() - emissionDate.getTime()) / (1000 * 3600 * 24);
        long daysBetweenEmissionAndDiscount = (discountDate.getTime() - emissionDate.getTime()) / (1000 * 3600 * 24);

        if (daysBetweenEmissionAndDue <= 0) {
            throw new IllegalArgumentException("Due date must be after emission date");
        }

        double tceaValue = Math.pow(
                (1 + ((adjustedNominalRate.doubleValue() / amount.doubleValue())) / (1 - ((double) daysBetweenEmissionAndDiscount / 360))),
                ((double) 360 / daysBetweenEmissionAndDue)
        ) - 1;

        return BigDecimal.valueOf(tceaValue).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}