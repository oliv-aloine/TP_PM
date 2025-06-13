import banco.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ContaPoupancaTest {
    private ContaPoupanca conta;
    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente("JohnDarkSouls", "1234567", "senhaboa");
        conta = new ContaPoupanca(cliente);
    }

    @Test
    public void testDepositoComRendimento() {
        conta.depositar(1000.0);
        double saldoEsperado = 1000.0 + (1000.0 * 0.006); // Rendimento de 0,6%
        assertEquals(saldoEsperado, conta.consultarSaldo(), 0.001);
    }

    @Test
    public void testSaqueComSaldoSuficiente() {
        conta.depositar(1000.0);
        conta.sacar(500.0);
        double saldoComRendimento = 1000.0 + (1000.0 * 0.006);
        double saldoEsperado = saldoComRendimento - 500.0;
        assertEquals(saldoEsperado, conta.consultarSaldo(), 0.001);
    }

    @Test
    public void testSaqueComSaldoInsuficiente() {
        conta.sacar(200.0);
        assertEquals(0.0, conta.consultarSaldo(), 0.001);
        // Nenhum saque deve ser registrado
        assertTrue(conta.consultarExtrato().isEmpty());
    }

    @Test
    public void testExtrato() {
        conta.depositar(1000.0);
        conta.sacar(500.0);
        List<String> extrato = conta.consultarExtrato();

        assertEquals(3, extrato.size()); // depósito, rendimento, saque

        assertTrue(extrato.get(0).contains("Depósito"));
        assertTrue(extrato.get(1).contains("Rendimento"));
        assertTrue(extrato.get(2).contains("Saque"));
    }
}
