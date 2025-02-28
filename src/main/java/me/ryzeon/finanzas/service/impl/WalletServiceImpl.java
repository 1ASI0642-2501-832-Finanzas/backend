package me.ryzeon.finanzas.service.impl;

import lombok.RequiredArgsConstructor;
import me.ryzeon.finanzas.dto.CreateWalletRequest;
import me.ryzeon.finanzas.dto.WalletDto;
import me.ryzeon.finanzas.entity.User;
import me.ryzeon.finanzas.entity.Wallet;
import me.ryzeon.finanzas.repository.WalletRepository;
import me.ryzeon.finanzas.service.UserService;
import me.ryzeon.finanzas.service.WalletService;
import org.springframework.stereotype.Service;

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
}
