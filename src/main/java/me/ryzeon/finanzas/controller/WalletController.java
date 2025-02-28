package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.CreateWalletRequest;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String createWallet(@RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request).orElse(null);
        if (wallet == null) {
            return "redirect:/";
        }
        return "redirect:/api/v1/wallet/" + wallet.getId();
    }

    @DeleteMapping("{id}")
    public void deleteWallet(@PathVariable Long id) {
        walletService.deleteWalletById(id);
    }
}
