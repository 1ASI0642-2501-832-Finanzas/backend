package me.ryzeon.finanzas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.ryzeon.finanzas.entity.Costs;
import me.ryzeon.finanzas.entity.Invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 01/03/25 @ 17:32
 */
public record InvoiceDto(

        Long id,

        @NotBlank
        String invoiceType,

        @NotBlank
        String financialInstitutionName,

        @NotBlank
        String number,

        @NotBlank
        String series,

        @NotBlank
        String issuerName,

        @NotBlank
        String issuerRuc,

        @NotBlank
        String currency,

        @NotNull
        BigDecimal amount,

        @NotNull
        BigDecimal igv,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @NotNull
        Date emissionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @NotNull
        Date dueDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @NotNull
        Date discountDate,

        @NotBlank
        String terms,

        @NotNull
        BigDecimal nominalRate,

        @NotNull
        BigDecimal effectiveRate,

        @NotNull
        List<Costs> initialCosts,

        @NotNull
        List<Costs> finalCosts,

        @NotBlank
        String status,

        @NotNull
        BigDecimal tcea
) {
    public InvoiceDto(Invoice invoice) {
        this(
                invoice.getId(),
                invoice.getInvoiceType(),
                invoice.getFinancialInstitutionName(),
                invoice.getNumber(),
                invoice.getSeries(),
                invoice.getIssuerName(),
                invoice.getIssuerRuc(),
                invoice.getCurrency(),
                invoice.getAmount(),
                invoice.getIgv(),
                invoice.getEmissionDate(),
                invoice.getDueDate(),
                invoice.getDiscountDate(),
                invoice.getTerms(),
                invoice.getNominalRate(),
                invoice.getEffectiveRate(),
                invoice.getInitialCosts(),
                invoice.getFinalCosts(),
                invoice.getStatus(),
                invoice.getTcea()
        );
    }
}