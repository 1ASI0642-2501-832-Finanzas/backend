package me.ryzeon.finanzas.service.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import me.ryzeon.finanzas.dto.CreateWalletRequest;
import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.entity.User;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.repository.InvoiceRepository;
import me.ryzeon.finanzas.repository.WalletRepository;
import me.ryzeon.finanzas.service.UserService;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:48
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    public final WalletRepository walletRepository;
    private final UserService userService;
    private final InvoiceRepository invoiceRepository;

    @Override
    public Optional<Wallet> createWallet(CreateWalletRequest request) {
        User user = userService.findByIdentifier(request.userIdentifier()).orElseThrow();
        Wallet wallet = Wallet.builder()
                .name(request.name())
                .description(request.description())
                .discountDate(request.discountDate())
                .tcea(BigDecimal.ZERO)
                .user(user)
                .build();

        return Optional.of(walletRepository.save(wallet));
    }

    @Override
    public List<Wallet> getWalletsByUserIdentifier(String identifier) {
        User user = userService.findByIdentifier(identifier).orElseThrow();
        return walletRepository.findAllByUser(user);
    }

    @Override
    public void deleteWalletById(Long id) {
        walletRepository.deleteById(id);
    }

    @Override
    public Optional<Wallet> getWalletById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public void updateTcea(Wallet wallet) {
        List<Invoice> invoices = invoiceRepository.findAllByWallet(wallet);

        BigDecimal totalWeightedTCEA = invoices.stream()
                .map(invoice -> invoice.getTcea().multiply(invoice.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = invoices.stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tcea = totalWeightedTCEA.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP);

        wallet.setTcea(tcea);

        walletRepository.save(wallet);
    }

    @Override
    public ResponseEntity<Resource> generateReport(Long id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        if (walletOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Wallet wallet = walletOptional.get();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            PdfFont bold = PdfFontFactory.createFont("Helvetica-Bold");

            document.add(new Paragraph("WALLET REPORT").setFontSize(18).setFont(bold).setFontColor(ColorConstants.BLUE));
            document.add(new Paragraph("Generated Date: " + java.time.LocalDate.now()).setFontSize(10));
            document.add(new Paragraph("Wallet Name: " + wallet.getName()).simulateBold());
            document.add(new Paragraph("Description: " + wallet.getDescription()));
            document.add(new Paragraph("Discount Date: " + wallet.getDiscountDate()));
            document.add(new Paragraph("TCEA: " + wallet.getTcea() + "%"));
            document.add(new Paragraph(" "));

            float[] columnWidths = {4, 3, 3, 3, 3, 2, 2, 3};
            Table table = new Table(columnWidths);
            table.addHeaderCell(new Cell().add(new Paragraph("Invoice #").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("Series").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("Issuer").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("IGV").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("Due Date").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("TCEA").setFont(bold)));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setFont(bold)));

            List<Invoice> invoices = invoiceRepository.findAllByWallet(wallet);
            for (Invoice invoice : invoices) {
                table.addCell(new Cell().add(new Paragraph(invoice.getNumber())));
                table.addCell(new Cell().add(new Paragraph(invoice.getSeries())));
                table.addCell(new Cell().add(new Paragraph(invoice.getIssuerName() + " (" + invoice.getIssuerRuc() + ")")));
                table.addCell(new Cell().add(new Paragraph("£" + invoice.getAmount())));
                table.addCell(new Cell().add(new Paragraph("£" + invoice.getIgv())));
                table.addCell(new Cell().add(new Paragraph(invoice.getDueDate().toString())));
                table.addCell(new Cell().add(new Paragraph(invoice.getTcea() + "%")));
                table.addCell(new Cell().add(new Paragraph(invoice.getStatus())));
            }

            document.add(table);
            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
