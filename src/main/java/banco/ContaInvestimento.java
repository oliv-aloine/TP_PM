package banco;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContaInvestimento implements Conta {
    private Cliente cliente;
    private double saldo;
    private List<String> extrato;
    private static final TipoConta tipo = TipoConta.INVESTIMENTO;
    private static final double IMPOSTO_SAQUE = 0.225; // 22.5%
    private static final double TAXA_ADMINISTRATIVA = 0.01;
    private static final double RENDIMENTO_MIN = -0.006;
    private static final double RENDIMENTO_MAX = 0.015;
    private Random random;

    public ContaInvestimento(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = 0;
        this.extrato = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public double consultarSaldo() {
        return saldo;
    }

    @Override
    public void depositar(double valor) {
        this.saldo += valor;
        extrato.add("Depósito: +R$" + valor);
    }

    @Override
    public void sacar(double valor) {
        double rendimento = aplicarRendimentoMensal();
        double imposto = rendimento > 0 ? rendimento * IMPOSTO_SAQUE : 0;
        double totalDescontado = valor + imposto;

        if (totalDescontado > saldo) {
            return;
        }

        saldo -= totalDescontado;
        extrato.add("Saque: -R$" + valor);
        if (imposto > 0) {
            extrato.add("Imposto sobre rendimento: -R$" + imposto);
        }
    }

    private double aplicarRendimentoMensal() {
        double taxaRendimento = RENDIMENTO_MIN + (RENDIMENTO_MAX - RENDIMENTO_MIN) * random.nextDouble();
        double rendimento = saldo * taxaRendimento;
        saldo += rendimento;
        String sinal = rendimento >= 0 ? "+" : "";
        extrato.add("Rendimento aplicado (" + (taxaRendimento * 100) + "%): " + sinal + "R$" + rendimento);

        if (rendimento > 0) {
            double taxaAdmin = rendimento * TAXA_ADMINISTRATIVA;
            saldo -= taxaAdmin;
            extrato.add("Taxa administrativa: -R$" + taxaAdmin);
        }

        return rendimento;
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
