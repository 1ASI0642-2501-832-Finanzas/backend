package me.ryzeon.finanzas;

import me.ryzeon.finanzas.dto.CostsDto;
import me.ryzeon.finanzas.dto.CreateInvoiceRequest;
import me.ryzeon.finanzas.entity.User;
import me.ryzeon.finanzas.entity.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanzasApplicationTests {

    private User mockUser;
    private Wallet mockWallet;
    private CreateInvoiceRequest invoiceRequest;

    @BeforeEach
    void setup()  {
        mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getId()).thenReturn(1L);
        Mockito.when(mockUser.getEmail()).thenReturn("dev@ryzeon.me");

        mockWallet = Mockito.mock(Wallet.class);
        Mockito.when(mockWallet.getId()).thenReturn(1L);
        Mockito.when(mockWallet.getName()).thenReturn("Test Wallet");
        Mockito.when(mockWallet.getDescription()).thenReturn("Test Wallet Description");
        Date now = new Date();
        Mockito.when(mockWallet.getDiscountDate()).thenReturn(now);
        Mockito.when(mockWallet.getUser()).thenReturn(mockUser);

        CostsDto initialCosts = new CostsDto("GESTION", new BigDecimal(200.00), "efectivo");
        CostsDto finalCosts = new CostsDto("GESTION", new BigDecimal(150.00), "efectivo");


        invoiceRequest = new CreateInvoiceRequest(
                "Factura",
                "Banco",
                "001",
                "F001",
                "EmpresaX",
                "12345678",
                "Soles(PEN)",
                new BigDecimal("10000"),
                new Date(2025 - 1900, 1, 1),  // Emission Date (2025-02-01)
                new Date(2025 - 1900, 6, 10), // Due Date (2025-07-10)
                new Date(2025 - 1900, 3, 5),  // Discount Date (2025-04-05)
                "NI IDIEA Pa",
                new BigDecimal(12),
                new BigDecimal(
                30),
                List.of(initialCosts),
                List.of(finalCosts),
                "En progreso",
                mockWallet.getId()
        );
    }

    @Test
    void contextLoads() {
        // Ejecutar el cálculo de TCEA
        BigDecimal tcea = invoiceRequest.calculateTCEA();

        // Valor esperado
        BigDecimal expectedTCEA = new BigDecimal("359.58");

        // Validar que el TCEA sea correcto
        assertNotNull(tcea, "TCEA no debería ser null");
        assertTrue(tcea.compareTo(BigDecimal.ZERO) > 0, "TCEA debe ser positiva");
        assertEquals(expectedTCEA, tcea, "TCEA calculado no coincide con el esperado");

        // Imprimir en consola
        System.out.println("TCEA Calculado: " + tcea);
    }

}
