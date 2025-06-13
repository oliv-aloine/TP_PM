package banco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Banco {
    public List<Cliente> clientes;
    public List<Conta> contas;

    public Banco() {
        clientes = new ArrayList<>();
        contas = new ArrayList<>();
    }

    public void criarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void criarConta(Cliente cliente, TipoConta tipoConta, double... parametros) {
        Conta conta;
        switch (tipoConta) {
            case CORRENTE:
                conta = new ContaCorrente(cliente, parametros[0]);
                break;
            case POUPANCA:
                conta = new ContaPoupanca(cliente);
                break;
            case RENDA_FIXA:
                conta = new ContaRendaFixa(cliente);
                break;
            case INVESTIMENTO:
                conta = new ContaInvestimento(cliente);
                break;
            default:
                throw new IllegalArgumentException("Tipo inválido.");
        }
        contas.add(conta);
    }

    public Conta buscarConta(Cliente cliente, TipoConta tipo) {
        for (Conta conta : contas) {
            if (conta.getCliente().equals(cliente) && conta.getTipoConta() == tipo) {
                return conta;
            }
        }
        return null;
    }

    public List<String> consultarExtrato(Cliente cliente, Conta conta) {
        if (conta.getCliente() == cliente) {
            return conta.consultarExtrato();
        }
        return new ArrayList<>();
    }

    public double calcularSaldoMedio() {
        double totalSaldo = 0;
        int totalContas = contas.size();

        for (Conta conta : contas) {
            totalSaldo += conta.consultarSaldo();
        }

        if (totalContas == 0) {
            return 0;
        }
        return totalSaldo / totalContas;
    }

    public int contarClientesComSaldoNegativo() {
        int contador = 0;
        for (Cliente cliente : clientes) {
            double saldoTotal = 0;
            for (Conta conta : contas) {
                if (conta.getCliente().equals(cliente)) {
                    saldoTotal += conta.consultarSaldo();
                }
            }
            if (saldoTotal < 0) {
                contador++;
            }
        }
        return contador;
    }

    public Cliente encontrarClienteComMaiorSaldo() {
        Cliente clienteMaiorSaldo = null;
        double maiorSaldo = Double.NEGATIVE_INFINITY;

        for (Cliente cliente : clientes) {
            double saldoTotal = 0;
            for (Conta conta : contas) {
                if (conta.getCliente().equals(cliente)) {
                    saldoTotal += conta.consultarSaldo();
                }
            }

            if (saldoTotal > maiorSaldo) {
                maiorSaldo = saldoTotal;
                clienteMaiorSaldo = cliente;
            }
        }
        return clienteMaiorSaldo;
    }

    public Cliente encontrarClienteComMenorSaldo() {
        Cliente clienteMenorSaldo = null;
        double menorSaldo = Double.POSITIVE_INFINITY;

        for (Cliente cliente : clientes) {
            double saldoTotal = 0;
            for (Conta conta : contas) {
                if (conta.getCliente().equals(cliente)) {
                    saldoTotal += conta.consultarSaldo();
                }
            }

            if (saldoTotal < menorSaldo) {
                menorSaldo = saldoTotal;
                clienteMenorSaldo = cliente;
            }
        }
        return clienteMenorSaldo;
    }

    public Map<String, Double> calcularCustodiaPorTipo() {
        Map<String, Double> custodiaPorTipo = new HashMap<>();

        for (Conta conta : contas) {
            String tipo = conta.getClass().getSimpleName();
            double saldo = conta.consultarSaldo();

            custodiaPorTipo.put(tipo, custodiaPorTipo.getOrDefault(tipo, 0.0) + saldo);
        }
        return custodiaPorTipo;
    }
}