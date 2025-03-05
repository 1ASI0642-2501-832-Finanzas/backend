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

    private Date emissionDate;

    private Date dueDate;

    private Date discountDate;

    private String terms;

    @Column(precision = 5, scale = 2)
    private BigDecimal effectiveRate; // Tasa Efectiva

    @Column(precision = 5, scale = 2)
    private BigDecimal tepDays; // Tasa Efectiva en días


    @ManyToMany
    @JoinTable(
            name = "initial_costs",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "costs_id")
    )
    private List<Costs> initialCosts;

    @ManyToMany
    @JoinTable(
            name = "final_costs",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "costs_id")
    )
    private List<Costs> finalCosts;

    private String status;

    @Column(precision = 10, scale = 2)
    private BigDecimal tcea;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;
}
