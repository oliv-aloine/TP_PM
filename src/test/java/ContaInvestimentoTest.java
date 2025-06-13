import banco.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ContaInvestimentoTest {
    private ContaInvestimento conta;
    private Cliente cliente;

    @BeforeEach
    public void setUp() throws Exception {
        cliente = new Cliente("JohnDarkSouls", "1234567", "senhaboa");
        conta = new ContaInvestimento(cliente);

        // Substituindo Random da conta por um fixo que retorna sempre 0.5 para testes estáveis
        Field randomField = ContaInvestimento.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(conta, new RandomFixo());
    }

    @Test
    public void testDeposito() {
        conta.depositar(2000);
        assertEquals(2000, conta.consultarSaldo(), 0.1);
        List<String> extrato = conta.consultarExtrato();
        assertTrue(extrato.get(0).contains("Depósito"));
    }

    @Test
    public void testSaqueComRendimentoPositivo() {
        conta.depositar(1000);
        conta.sacar(500); // Com rendimento e imposto aplicados

        List<String> extrato = conta.consultarExtrato();
        assertEquals(5, extrato.size()); // Depósito, Rendimento, Taxa, Saque, Imposto

        assertTrue(extrato.get(0).contains("Depósito"));
        assertTrue(extrato.get(1).contains("Rendimento aplicado"));
        assertTrue(extrato.get(2).contains("Taxa administrativa"));
        assertTrue(extrato.get(3).contains("Saque"));
        assertTrue(extrato.get(4).contains("Imposto"));
    }

    @Test
    public void testSaqueComSaldoInsuficiente() {
        conta.depositar(100);
        conta.sacar(150); // Deve falhar (saldo insuficiente após imposto)

        double esperado = 100 + (100 * 0.0045) - (100 * 0.0045 * 0.01);
        assertEquals(esperado, conta.consultarSaldo(), 0.0001);

        List<String> extrato = conta.consultarExtrato();
        assertEquals(3, extrato.size()); // Depósito, rendimento, taxa admin
        assertTrue(extrato.get(1).contains("Rendimento"));
    }

    // Classe auxiliar para simular Random fixo
    static class RandomFixo extends Random {
        @Override
        public double nextDouble() {
            return 0.5; // Fixo para garantir previsibilidade nos testes
        }
    }
}