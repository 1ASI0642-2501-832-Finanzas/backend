package me.ryzeon.finanzas.service;

import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.dto.InvoiceDto;
import me.ryzeon.finanzas.entity.Invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 05:07
 */
public interface InvoiceService {

    void deleteInvoice(Long id);

    @Transactional
    Optional<Invoice> createInvoice(CreateInvoiceRequest request);

    Optional<Invoice> getInvoiceById(Long id);

    Optional<Invoice> updateInvoice(Long id, CreateInvoiceRequest request);

    List<InvoiceDto> getInvoicesByWalletId(Long walletId);
}
