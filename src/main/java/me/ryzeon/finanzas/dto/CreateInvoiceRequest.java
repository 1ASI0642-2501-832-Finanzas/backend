package me.ryzeon.finanzas.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    /**
     * Calculates the Annual Effective Cost Rate (TCEA) based on the invoice data.
     *
     * @return The TCEA as a percentage with four decimal places.
     * @throws IllegalArgumentException If required values are null or invalid.
     */
    public BigDecimal calculateTCEA() {
        if (effectiveRate == null || amount == null || dueDate == null || discountDate == null) {
            throw new IllegalArgumentException("Effective rate, amount, or dates cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        long discountDays = (dueDate.getTime() - discountDate.getTime()) / (1000 * 3600 * 24);
        if (discountDays <= 0) {
            throw new IllegalArgumentException("Due date must be after discount date");
        }

        // Nominal Value (VN)
        BigDecimal nominalValue = amount;

        // Effective annual rate in decimal format
        BigDecimal effectiveRateDecimal = effectiveRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // Discount Calculation (D)
        BigDecimal discount = nominalValue.multiply(effectiveRateDecimal.multiply(BigDecimal.valueOf(discountDays)).divide(BigDecimal.valueOf(360), 10, RoundingMode.HALF_UP));

        // Delivered Value (VE)
        BigDecimal deliveredValue = nominalValue.subtract(discount);

        // Initial Costs (CI)
        BigDecimal initialCostsTotal = initialCosts != null ? initialCosts.stream()
                .map(cost -> cost.type().equals("percentage") ?
                        nominalValue.multiply(cost.value().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP))
                        : cost.value())
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        // Final Costs (CF)
        BigDecimal finalCostsTotal = finalCosts != null ? finalCosts.stream()
                .map(cost -> cost.type().equals("percentage") ? nominalValue.multiply(cost.value().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)) : cost.value())
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        // Net Received Value (VNR)
        BigDecimal netReceivedValue = deliveredValue.subtract(initialCostsTotal);

        // Net Paid Value (VNP)
        BigDecimal netPaidValue = nominalValue.add(finalCostsTotal);

        if (netReceivedValue.compareTo(BigDecimal.ZERO) <= 0 || netPaidValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("VNR or VNP is less than or equal to zero, cannot calculate TCEA");
        }

        // TCEA Calculation
        double exponent = 360.0 / discountDays;
        double tceaCalculated = Math.pow(netPaidValue.divide(netReceivedValue, 10, RoundingMode.HALF_UP).doubleValue(), exponent) - 1;

        return BigDecimal.valueOf(tceaCalculated).multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);
    }
}