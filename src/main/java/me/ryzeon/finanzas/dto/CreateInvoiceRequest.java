package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Emission date in the format dd-MM-yyyy", example = "28-02-2025")
        Date emissionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Due date in the format dd-MM-yyyy", example = "28-02-2025")
        Date dueDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Discount date in the format dd-MM-yyyy", example = "28-02-2025")
        Date discountDate,

        String terms,
        BigDecimal effectiveRate,
        BigDecimal tepDays,
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

        BigDecimal effectiveRate = this.effectiveRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        log.info("Effective Rate: {}", effectiveRate);

        long discountDays = (dueDate.getTime() - discountDate.getTime()) / (1000 * 60 * 60 * 24);

        log.info("Días calculados entre dueDate y discountDate: " + discountDays);

        if (discountDays <= 0) {
            throw new IllegalArgumentException("Due date must be after discount date");
        }

        if (discountDays < 5) {
            throw new IllegalArgumentException("Discount period is too short, leads to extreme TCEA values.");
        }


        BigDecimal exponent = BigDecimal.valueOf(discountDays).divide(tepDays, 10, RoundingMode.HALF_UP);
        log.info("Valor de exponente: {}", exponent);

        BigDecimal ten2 = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(effectiveRate).doubleValue(), exponent.doubleValue())).subtract(BigDecimal.ONE);
        log.info("Valor de ten2: {}", ten2);


        log.info("Valor de ten2 (%): {}", ten2.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));

        BigDecimal dPercent = ten2.divide(BigDecimal.ONE.add(ten2), 10, RoundingMode.HALF_UP);
        log.info("Valor de dPercent: {}", dPercent);
        log.info("Valor de dPercent (%): {}", dPercent.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));

        BigDecimal netValue = amount.multiply(BigDecimal.ONE.subtract(dPercent));
        log.info("Valor de netValue: {}", netValue.setScale(2, RoundingMode.HALF_UP));

        BigDecimal discountInterest = amount.subtract(netValue);
        log.info("Valor de discountInterest: {}", discountInterest.setScale(2, RoundingMode.HALF_UP));

        BigDecimal totalInitialCosts = initialCosts.stream()
                .map(CostsDto::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Total Initial Costs: {}", totalInitialCosts);

        BigDecimal totalFinalCosts = finalCosts.stream()
                .map(CostsDto::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Total Final Costs: {}", totalFinalCosts);

        BigDecimal receivedValue = netValue.subtract(totalInitialCosts);
        BigDecimal shipValue = amount.add(totalFinalCosts);

        if (receivedValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Received value must be greater than zero");
        }

        log.info("Received Value: {}", receivedValue);
        log.info("Ship Value: {}", shipValue);

        BigDecimal ratio = shipValue.divide(receivedValue, 10, RoundingMode.HALF_UP);
        double exponentTCEA = 360.0 / discountDays;
        BigDecimal tcea = BigDecimal.valueOf(Math.pow(ratio.doubleValue(), exponentTCEA))
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100));

        log.info("TCEA Calculado: {}", tcea);

        return tcea.setScale(2, RoundingMode.HALF_UP);
    }
}
