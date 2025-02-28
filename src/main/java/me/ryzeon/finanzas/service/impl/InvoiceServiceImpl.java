package me.ryzeon.finanzas.service.impl;

import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CostsDto;
import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.repository.InvoiceRepository;
import me.ryzeon.finanzas.service.InvoiceService;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.stereotype.Service;

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

    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<Invoice> createInvoice(CreateInvoiceRequest request) {
        Wallet wallet = walletService.getWalletById(request.walletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));
        Invoice invoice = Invoice.builder()
                .invoiceType(request.invoiceType())
                .number(request.number())
                .series(request.series())
                .issuerName(request.issuerName())
                .issuerRuc(request.issuerRuc())
                .currency(request.currency())
                .amount(request.amount())
                .igv(request.igv())
                .emissionDate(request.emissionDate())
                .dueDate(request.dueDate())
                .discountDate(request.discountDate())
                .terms(request.terms())
                .nominalRate(request.nominalRate())
                .effectiveRate(request.effectiveRate())
                .initialCosts(
                        request.initialCosts()
                                .stream()
                                .map(CostsDto::toEntity)
                                .toList()
                )
                .finalCosts(
                        request.finalCosts()
                                .stream()
                                .map(CostsDto::toEntity)
                                .toList()
                )
                .status(request.status())
                .tcea(request.calculateTcea())
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
        Invoice invoice = Invoice.builder()
                .id(id)
                .invoiceType(request.invoiceType())
                .number(request.number())
                .series(request.series())
                .issuerName(request.issuerName())
                .issuerRuc(request.issuerRuc())
                .currency(request.currency())
                .amount(request.amount())
                .igv(request.igv())
                .emissionDate(request.emissionDate())
                .dueDate(request.dueDate())
                .discountDate(request.discountDate())
                .terms(request.terms())
                .nominalRate(request.nominalRate())
                .effectiveRate(request.effectiveRate())
                .initialCosts(
                        request.initialCosts()
                                .stream()
                                .map(CostsDto::toEntity)
                                .toList()
                )
                .finalCosts(
                        request.finalCosts()
                                .stream()
                                .map(CostsDto::toEntity)
                                .toList()
                )
                .status(request.status())
                .tcea(request.calculateTcea())
                .wallet(wallet)
                .build();

        Optional<Invoice> invoiceOptional = Optional.of(invoiceRepository.save(invoice));
        walletService.updateTcea(wallet);
        return invoiceOptional;
    }
}
