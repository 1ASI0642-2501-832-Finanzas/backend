package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody CreateInvoiceRequest request) {
        Optional<Invoice> invoice = invoiceService.createInvoice(request);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody CreateInvoiceRequest request) {
        Optional<Invoice> invoice = invoiceService.updateInvoice(id, request);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
