package me.ryzeon.finanzas.service;

import jakarta.transaction.Transactional;
import me.ryzeon.finanzas.dto.CreateWalletRequest;
import me.ryzeon.finanzas.entity.Wallet;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:47
 */
public interface WalletService {

    @Transactional
    Optional<Wallet> createWallet(CreateWalletRequest request);

    List<Wallet> getWalletsByUserIdentifier(String identifier);

    void deleteWalletById(Long id);

    Optional<Wallet> getWalletById(Long id);

    void updateTcea(Wallet wallet);

    ResponseEntity<Resource> generateReport(Long id);
}
