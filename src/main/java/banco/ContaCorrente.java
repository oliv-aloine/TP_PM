package banco;

import java.util.ArrayList;
import java.util.List;

public class ContaCorrente implements Conta{
    private Cliente cliente;
    private double saldo;

    private List<String> extrato;
    private double limiteUtilizado;
    private double limiteContratado;
    private static final TipoConta tipo = TipoConta.CORRENTE;
    private static final double taxaDeJuros = 0.03; //3% taxa de juros
    private static final double tarifaFixa = 10.0;  // 10 de tarifa fixa

    public ContaCorrente(Cliente cliente, double limiteContratado) {
        this.cliente = cliente;
        this.saldo = 0;
        this.extrato = new ArrayList<>();
        this.limiteUtilizado = 0;
        this.limiteContratado = limiteContratado;
    }

    @Override
    public double consultarSaldo() {
        return saldo;
    }

    private double calcularTaxa() {
        return Math.abs(this.saldo * taxaDeJuros) + tarifaFixa;
    }

    @Override
    public void depositar(double valor) {
        if (this.saldo < 0) {
            double taxa = calcularTaxa();
            this.saldo += valor - taxa; // desconta os 3% + tarifa fixa

            this.limiteUtilizado -= valor;
            if (this.limiteUtilizado < 0) {
                this.limiteUtilizado = 0;
            }
        } else {
            this.saldo += valor;
        }
        extrato.add("Depósito: +R$" + valor);
    }

    @Override
    public void sacar(double valor) {
        double max = this.saldo + (this.limiteContratado - this.limiteUtilizado);

        if (valor > max) {
            return;
        }

        if (valor > saldo) {
            double parteDoLimite = valor - saldo;
            limiteUtilizado += parteDoLimite;
            if (limiteUtilizado > limiteContratado) {
                limiteUtilizado = limiteContratado;
            }
            saldo -= valor;
        } else {
            saldo -= valor;
        }
        extrato.add("Saque: - R$" + valor);
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

    public double getLimiteUtilizado() {
        return limiteUtilizado;
    }

    public double getLimiteContratado() {
        return limiteContratado;
    }
}