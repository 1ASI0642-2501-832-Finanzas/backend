package me.ryzeon.finanzas.repository;

import me.ryzeon.finanzas.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 27/02/25 @ 09:46
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
