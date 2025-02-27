package me.ryzeon.finanzas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public HttpEntity<WalletDto> getWallet(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id).orElse(null);
        return wallet != null ? ResponseEntity.ok(WalletDto.from(wallet)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public String createWallet(@RequestBody WalletDto walletDto) {
        Wallet wallet = walletService.createWallet(walletDto).orElse(null);
        if (wallet == null) {
            return "redirect:/";
        }
        return "redirect:/api/v1/wallet/" + wallet.getId();
    }
}
