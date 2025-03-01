package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.dto.InvoiceDto;
import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 05:21
 */
@RestController
@RequestMapping("api/v1/invoice")
@Tag(name = "Invoice", description = "Endpoints para la gestión de facturas")
@AllArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @DeleteMapping("{id}")
    public void deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(InvoiceDto::new).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all/{walletId}")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByWalletId(@PathVariable Long walletId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByWalletId(walletId));
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody CreateInvoiceRequest request) {
        Optional<Invoice> invoice = invoiceService.createInvoice(request);
        InvoiceDto invoiceDto = new InvoiceDto(invoice.orElseThrow());
        return ResponseEntity.ok(invoiceDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable Long id, @RequestBody CreateInvoiceRequest request) {
        Optional<Invoice> invoice = invoiceService.updateInvoice(id, request);
        InvoiceDto invoiceDto = new InvoiceDto(invoice.orElseThrow());
        return ResponseEntity.ok(invoiceDto);
    }
}
