package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date emissionDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date dueDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date discountDate,
        String terms,
        BigDecimal nominalRate,
        BigDecimal effectiveRate,
        List<CostsDto> initialCosts,
        List<CostsDto> finalCosts,
        String status,
        Long walletId
) {

    private static final Logger log = LoggerFactory.getLogger(CreateInvoiceRequest.class);

    /**
     * Calculates the Annual Effective Cost Rate (TCEA) based on the invoice data.
     *
     * @return The TCEA as a percentage with two decimal places.
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
//        if (discountDays <= 0) {
//            throw new IllegalArgumentException("Due date must be after discount date");
//        }

        // Ensure discount period is reasonable
        if (discountDays < 5) {
            throw new IllegalArgumentException("Discount period is too short, leads to extreme TCEA values.");
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
                .map(cost -> cost.type().equalsIgnoreCase("percentage") ?
                        nominalValue.multiply(cost.value().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP))
                        : cost.value())
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        // Final Costs (CF)
        BigDecimal finalCostsTotal = finalCosts != null ? finalCosts.stream()
                .map(cost -> cost.type().equalsIgnoreCase("percentage") ? nominalValue.multiply(cost.value().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)) : cost.value())
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        // Net Received Value (VNR)
        BigDecimal netReceivedValue = deliveredValue.subtract(initialCostsTotal);

        // Net Paid Value (VNP)
        BigDecimal netPaidValue = nominalValue.add(finalCostsTotal);

        if (netReceivedValue.compareTo(BigDecimal.valueOf(10)) < 0) {
            throw new IllegalArgumentException("Net Received Value (VNR) is too small, leading to an unrealistic TCEA.");
        }

        // Ensure VNP/VNR ratio is reasonable
        BigDecimal ratio = netPaidValue.divide(netReceivedValue, 10, RoundingMode.HALF_UP);
        if (ratio.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Ratio too high, possible calculation error.");
        }

        // TCEA Calculation
        double exponent = 360.0 / discountDays;
        double tceaCalculated = Math.pow(ratio.doubleValue(), exponent) - 1;

        if (Double.isNaN(tceaCalculated) || Double.isInfinite(tceaCalculated) || tceaCalculated * 100 > 100) {
            log.error("Calculated TCEA is unrealistically high: {}", tceaCalculated);
            throw new IllegalArgumentException("Calculated TCEA is unrealistically high.");
        }

        BigDecimal tcea = BigDecimal.valueOf(tceaCalculated).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        log.info("TCEA calculated: {}", tcea);
        return tcea;
    }


}