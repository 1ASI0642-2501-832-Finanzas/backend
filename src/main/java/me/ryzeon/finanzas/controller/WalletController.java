package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CreateWalletRequest;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:50
 */
@RestController
@RequestMapping("api/v1/wallet")
@Tag(name = "Wallet", description = "Endpoints para la gestión de billeteras")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/users/{userIdentifier}")
    public HttpEntity<List<WalletDto>> getWallet(@PathVariable String userIdentifier) {
        List<Wallet> wallets = walletService.getWalletsByUserIdentifier(userIdentifier);
        if (wallets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(wallets.stream().map(WalletDto::new).toList());
    }

    @GetMapping("{id}")
    public HttpEntity<WalletDto> getWalletById(@PathVariable Long id) {
        return walletService.getWalletById(id)
                .map(WalletDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createWallet(@RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request).orElse(null);
        if (wallet == null) {
            return ResponseEntity.badRequest().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(wallet.getId())
                .toUri();
        WalletDto walletDto = new WalletDto(wallet);
        return ResponseEntity.created(location).body(walletDto);
    }

    @DeleteMapping("{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.deleteWalletById(id);
    }

    @Operation(summary = "Generates a PDF report for a wallet", description = "Returns a PDF report with wallet details and invoices")
    @GetMapping("/{id}/report")
    public ResponseEntity<Resource> generateReport(@PathVariable Long id) {
        return walletService.generateReport(id);
    }
}
