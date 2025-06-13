import banco.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContaCorrenteTest {

    private Cliente cliente;
    private ContaCorrente conta;

    @BeforeEach
    public void setup() {
        cliente = new Cliente("JohnDarkSouls", "1234567", "senhaboa");
        conta = new ContaCorrente(cliente, 500);
    }

    @Test
    public void testDepositarSemTaxa() {
        conta.depositar(1000);
        assertEquals(1000, conta.consultarSaldo());
    }

    @Test
    public void testSacarDentroDoLimiteContratado() {
        conta.depositar(1000);
        conta.sacar(1200); // usa limite 200

        assertEquals(-200, conta.consultarSaldo());
        assertEquals(200, conta.getLimiteUtilizado());
    }

    @Test
    public void testSacarAcimaDoLimite() {
        conta.depositar(1000);
        conta.sacar(1600); // limite 500

        // saldo e limite não muda
        assertEquals(1000, conta.consultarSaldo());
        assertEquals(0, conta.getLimiteUtilizado());
    }

    @Test
    public void testDepositarComTaxaQuandoSaldoNegativo() {
        conta.depositar(1000);
        conta.sacar(1300); // limite utilizado 300

        conta.depositar(400);

        // -300 + 400 - 19 = 81
        assertEquals(81, conta.consultarSaldo());

        // limite utilizado atualizado
        assertEquals(0, conta.getLimiteUtilizado());
    }

    @Test
    public void testConsultarExtrato() {
        conta.depositar(1000);
        conta.depositar(500);
        conta.depositar(200);
        assertEquals(1700, conta.consultarSaldo());

        conta.sacar(300);
        conta.sacar(500);
        assertEquals(900, conta.consultarSaldo(), 0.01);

        // Verifica extrato
        var extrato = conta.consultarExtrato();

        assertEquals(5, extrato.size());

        // Verifica strings do extrato
        assertTrue(extrato.get(0).contains("Depósito: +R$1000"));
        assertTrue(extrato.get(1).contains("Depósito: +R$500"));
        assertTrue(extrato.get(2).contains("Depósito: +R$200"));
        assertTrue(extrato.get(3).contains("Saque: - R$300"));
        assertTrue(extrato.get(4).contains("Saque: - R$500"));
    }
}