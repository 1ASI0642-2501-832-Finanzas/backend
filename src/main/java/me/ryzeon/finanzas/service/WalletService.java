package me.ryzeon.finanzas.service;

import jakarta.transaction.Transactional;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.Wallet;

import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:47
 */
public interface WalletService {

    @Transactional
    Optional<Wallet> createWallet(WalletDto walletDto);

    Optional<Wallet> getWalletById(Long id);
}
