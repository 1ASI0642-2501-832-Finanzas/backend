package me.ryzeon.finanzas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CostsDto;
import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.dto.InvoiceDto;
import me.ryzeon.finanzas.entity.Costs;
import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.repository.InvoiceRepository;
import me.ryzeon.finanzas.service.CostsService;
import me.ryzeon.finanzas.service.InvoiceService;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 05:08
 */
@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final WalletService walletService;
    private final CostsService costsService;

    @Override
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Invoice not found");
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<Invoice> createInvoice(CreateInvoiceRequest request) {
        Wallet wallet = walletService.getWalletById(request.walletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));
        Invoice invoice = Invoice.builder()
                .invoiceType(request.invoiceType())
                .financialInstitutionName(request.financialInstitutionName())
                .number(request.number())
                .series(request.series())
                .issuerName(request.issuerName())
                .issuerRuc(request.issuerRuc())
                .currency(request.currency())
                .amount(request.amount())
                .emissionDate(request.emissionDate())
                .dueDate(request.dueDate())
                .discountDate(request.discountDate())
                .terms(request.terms())
                .effectiveRate(request.effectiveRate())
                .tepDays(request.tepDays())
                .initialCosts(
                        costsService.saveAll(
                                request.initialCosts()
                                        .stream()
                                        .map(CostsDto::toEntity)
                                        .toList()
                        )
                )
                .finalCosts(
                        costsService.saveAll(
                                request.finalCosts()
                                        .stream()
                                        .map(CostsDto::toEntity)
                                        .toList()
                        )
                )
                .status(request.status())
                .tcea(request.calculateTCEA())
                .wallet(wallet)
                .build();

        Optional<Invoice> invoiceOptional = Optional.of(invoiceRepository.save(invoice));
        walletService.updateTcea(wallet);
        return invoiceOptional;
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        if (invoiceRepository.existsById(id)) {
            return invoiceRepository.findById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Invoice> updateInvoice(Long id, CreateInvoiceRequest request) {
        Wallet wallet = walletService.getWalletById(request.walletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        Invoice existingInvoice = invoiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        existingInvoice.getFinalCosts().clear();
        existingInvoice.getInitialCosts().clear();


        invoiceRepository.save(existingInvoice);

        List<Costs> newInitialCosts = costsService.saveAll(
                request.initialCosts().stream().map(CostsDto::toEntity).toList()
        );

        List<Costs> newFinalCosts = costsService.saveAll(
                request.finalCosts().stream().map(CostsDto::toEntity).toList()
        );

        Invoice invoice = Invoice.builder()
                .id(id)
                .invoiceType(request.invoiceType())
                .financialInstitutionName(request.financialInstitutionName())
                .number(request.number())
                .series(request.series())
                .issuerName(request.issuerName())
                .issuerRuc(request.issuerRuc())
                .currency(request.currency())
                .amount(request.amount())
                .emissionDate(request.emissionDate())
                .dueDate(request.dueDate())
                .discountDate(request.discountDate())
                .terms(request.terms())
                .effectiveRate(request.effectiveRate())
                .tepDays(request.tepDays())
                .initialCosts(newInitialCosts)
                .finalCosts(newFinalCosts)
                .status(request.status())
                .tcea(request.calculateTCEA())
                .wallet(wallet)
                .build();

        if (invoice.getTcea().compareTo(new BigDecimal("99999.99")) > 0) {
            throw new IllegalArgumentException("TCEA value exceeds the maximum allowed value of 99999.99");
        }

        Optional<Invoice> invoiceOptional = Optional.of(invoiceRepository.save(invoice));
        walletService.updateTcea(wallet);
        return invoiceOptional;
    }

    @Override
    public List<InvoiceDto> getInvoicesByWalletId(Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId).orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
        List<Invoice> invoices = invoiceRepository.findAllByWallet(wallet);
        return invoices.stream().map(InvoiceDto::new).toList();
    }
}
