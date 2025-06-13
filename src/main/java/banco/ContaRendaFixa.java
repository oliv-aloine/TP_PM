package banco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContaRendaFixa implements Conta {
    private Cliente cliente;
    private double saldo;
    private List<String> extrato;
    private static final TipoConta tipo = TipoConta.RENDA_FIXA;
    private static final double IMPOSTO_SAQUE = 0.15;
    private static final double TARIFA_MENSAL = 20.0;
    private Random random = new Random();// 15% imposto saque

    public ContaRendaFixa(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = 0;
        this.extrato = new ArrayList<>();
    }

    @Override
    public double consultarSaldo() {
        return saldo;
    }

    @Override
    public void depositar(double valor) {
        this.saldo += valor;
        extrato.add("Depósito: +R$" + valor);

        // Aplica rendimento entre 0.50% e 0.85%
        double taxaRendimento = 0.005 + (0.0035 * random.nextDouble());
        double rendimento = this.saldo * taxaRendimento;
        this.saldo += rendimento;

        // Tarifa mensal
        this.saldo -= TARIFA_MENSAL;

        extrato.add("Rendimento mensal aplicado: +R$" + rendimento);
        extrato.add("Tarifa mensal: -R$" + TARIFA_MENSAL);
    }

    @Override
    public void sacar(double valor) {
        double imposto = valor * IMPOSTO_SAQUE;
        double total = valor + imposto;

        if (total > this.saldo) {
            return;
        }

        this.saldo -= total;
        extrato.add("Saque: -R$" + valor);
        extrato.add("Imposto de saque: -R$" + imposto);
    }

    @Override
    public List<String> consultarExtrato() {
        return extrato;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public TipoConta getTipoConta() {
        return tipo;
    }
}
