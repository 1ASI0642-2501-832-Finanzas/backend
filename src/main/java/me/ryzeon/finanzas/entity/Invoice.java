package me.ryzeon.finanzas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 03:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceType;

    private String financialInstitutionName;

    private String number;

    private String series;

    private String issuerName;

    private String issuerRuc;

    private String currency;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(precision = 15, scale = 2)
    private BigDecimal igv;

    private Date emissionDate;

    private Date dueDate;

    private Date discountDate;

    private String terms;

    @Column(precision = 5, scale = 2)
    private BigDecimal nominalRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal effectiveRate;


    @ManyToMany
    @JoinTable(
            name = "invoice_costs",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "costs_id")
    )
    private List<Costs> initialCosts;

    @ManyToMany
    @JoinTable(
            name = "invoice_costs",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "costs_id")
    )
    private List<Costs> finalCosts;

    private String status;

    @Column(precision = 7, scale = 2)
    private BigDecimal tcea;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;
}
