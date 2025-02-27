package me.ryzeon.finanzas.service.impl;

import lombok.RequiredArgsConstructor;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.repository.WalletRepository;
import me.ryzeon.finanzas.service.UserService;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Wallet> createWallet(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setName(walletDto.name());
        wallet.setDescription(walletDto.description());
        wallet.setDiscountDate(walletDto.discountDate());
        wallet.setUser(userService.findById(walletDto.userId()).orElseThrow());
        return Optional.of(walletRepository.save(wallet));
    }

    @Override
    public Optional<Wallet> getWalletById(Long id) {
        return walletRepository.findById(id);
    }
}
