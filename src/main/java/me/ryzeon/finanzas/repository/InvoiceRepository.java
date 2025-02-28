package me.ryzeon.finanzas.repository;

import me.ryzeon.finanzas.entity.Invoice;
import me.ryzeon.finanzas.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Alex Avila Asto - A.K.A (Ryzeon)
 * Project: finanzas
 * Date: 28/02/25 @ 04:23
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findAllByWallet(Wallet wallet);
}
