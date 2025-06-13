import banco.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ContaRendaFixaTest {
    private ContaRendaFixa conta;
    private Cliente cliente;

    @BeforeEach
    public void setUp() throws Exception {
        cliente = new Cliente("JohnDarkSouls", "1234567", "senhaboa");
        conta = new ContaRendaFixa(cliente);

        // Substitui Random da conta por um fixo que retorna sempre 0.5 para resultados previsíveis
        Field randomField = ContaRendaFixa.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(conta, new RandomFixo());
    }

    @Test
    public void testDepositoAplicaRendimentoETarifa() {
        conta.depositar(1000);

        // rendimento: 0.005 + (0.0035 * 0.5) = 0.00675
        double rendimento = 1000 * 0.00675; // = 6.75
        double saldoEsperado = 1000 + rendimento - 20; // = 986.75

        assertEquals(986.75, conta.consultarSaldo(), 0.0001);

        List<String> extrato = conta.consultarExtrato();
        assertEquals(3, extrato.size());
        assertTrue(extrato.get(0).contains("Depósito"));
        assertTrue(extrato.get(1).contains("Rendimento"));
        assertTrue(extrato.get(2).contains("Tarifa"));
    }

    @Test
    public void testSaqueComSaldoSuficiente() {
        conta.depositar(1000); // saldo final será 986.75 após rendimento e tarifa
        conta.sacar(800); // imposto = 120, total = 920

        double saldoEsperado = 986.75 - (800 + 800 * 0.15); // 986.75 - 920 = 66.75
        assertEquals(66.75, conta.consultarSaldo(), 0.0001);

        List<String> extrato = conta.consultarExtrato();
        assertEquals(5, extrato.size());
        assertTrue(extrato.get(3).contains("Saque"));
        assertTrue(extrato.get(4).contains("Imposto"));
    }

    @Test
    public void testSaqueComSaldoInsuficiente() {
        conta.depositar(100); // rendimento = 0.675, saldo = 100.675 - 20 = 80.675
        conta.sacar(100); // saque + imposto = 115 > 80.675 → não deve sacar

        assertEquals(80.675, conta.consultarSaldo(), 0.0001);

        List<String> extrato = conta.consultarExtrato();
        assertEquals(3, extrato.size()); // Depósito, Rendimento, Tarifa
    }

    // Random fixo para testes
    static class RandomFixo extends Random {
        @Override
        public double nextDouble() {
            return 0.5;
        }
    }
}
