package banco;

import java.util.ArrayList;
import java.util.List;

public class ContaPoupanca implements Conta {
    private Cliente cliente;
    private double saldo;
    private List<String> extrato;
    private static final TipoConta tipo = TipoConta.POUPANCA;
    private static final double RENDIMENTO_MENSAL = 0.006; // 0,60% mensal

    public ContaPoupanca(Cliente cliente) {
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

        double rendimento = this.saldo * RENDIMENTO_MENSAL;
        this.saldo += rendimento;
        extrato.add("Rendimento mensal: +R$" + rendimento);
    }

    @Override
    public void sacar(double valor) {
        if (valor > this.saldo) {
            return;
        }

        this.saldo -= valor;
        extrato.add("Saque: -R$" + valor);
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
    };
}
