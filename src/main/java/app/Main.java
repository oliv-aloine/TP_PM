package app;

import banco.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nBanco =) etc menu:");
            System.out.println("1. Criar Cliente");
            System.out.println("2. Criar Conta");
            System.out.println("3. Consultar Saldo");
            System.out.println("4. Depositar");
            System.out.println("5. Sacar");
            System.out.println("6. Extrato");
            System.out.println("7. Sair");
            System.out.println("\n8. Administração do Banco\n");
            System.out.print("Digite uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    //cria cliente
                    System.out.print("Nome cliente: ");
                    String nomeCliente = scanner.nextLine();
                    System.out.print("CPF cliente: ");
                    String cpfCliente = scanner.nextLine();
                    System.out.print("Senha cliente: ");
                    String senhaCliente = scanner.nextLine();

                    Cliente novo = new Cliente(nomeCliente, cpfCliente, senhaCliente);
                    banco.criarCliente(novo);

                    System.out.println("Cliente criado.");
                    break;

                case 2:
                    //cria conta
                    System.out.print("Digite o CPF do cliente: ");
                    String cpfClienteConta = scanner.nextLine();
                    Cliente clienteAssociado = null;
                    for (Cliente cliente : banco.clientes) {
                        if (cliente.getCpf().equals(cpfClienteConta)) {
                            clienteAssociado = cliente;
                            break;
                        }
                    }
                    if (clienteAssociado == null) {
                        System.out.println("Não encontrado.");
                        break;
                    }
                    System.out.println("Tipos de Conta:");
                    System.out.println("1. Conta Corrente");
                    System.out.println("2. Conta Poupança");
                    System.out.println("3. Conta Investimento");
                    System.out.println("4. Conta Renda Fixa");

                    System.out.print("Informe o tipo da conta: ");
                    int tipoConta = scanner.nextInt();
                    double[] parametros = null;

                    if (tipoConta == 1) {
                        // Conta Corrente – solicita limite contratado
                        System.out.print("Informe o limite de crédito: ");
                        double limite = scanner.nextDouble();
                        parametros = new double[]{limite};

                        TipoConta tipo = TipoConta.values()[tipoConta - 1];
                        banco.criarConta(clienteAssociado, tipo, parametros);
                        System.out.println("Conta criada.");
                        break;
                    }
                case 3:
                    //consulta saldo
                    System.out.print("Digite CPF do cliente: ");
                    String cpfClienteSaldo = scanner.nextLine();

                    Cliente clienteSaldo = null;
                    for (Cliente cliente : banco.clientes) {
                        if (cliente.getCpf().equals(cpfClienteSaldo)) {
                            clienteSaldo = cliente;
                            break;
                        }
                    }
                    if (clienteSaldo == null) {
                        System.out.println("Não encontrado.");
                        break;
                    }

                    for (Conta conta : banco.contas) {
                        if (conta.getCliente() == clienteSaldo) {
                            double saldo = conta.consultarSaldo();
                            System.out.println(clienteSaldo.getNome() + " possui saldo de: R$" + saldo);
                            break;
                        }
                    }
                    break;
                case 4:
                    //depósito
                    System.out.print("Digite CPF do cliente: ");
                    String cpfClienteDeposito = scanner.nextLine();

                    Cliente clienteDeposito = null;
                    for (Cliente cliente : banco.clientes) {
                        if (cliente.getCpf().equals(cpfClienteDeposito)) {
                            clienteDeposito = cliente;
                            break;
                        }
                    }
                    if (clienteDeposito == null) {
                        System.out.println("Não encontrado.");
                        break;
                    }

                    System.out.print("Informe o valor do depósito: ");
                    double valorDeposito = scanner.nextDouble();

                    if (valorDeposito <= 0) {
                        System.out.println("Insira um valor positivo.");
                        break;
                    }

                    for (Conta conta : banco.contas) {
                        if (conta.getCliente() == clienteDeposito) {
                            conta.depositar(valorDeposito);
                            System.out.println("R$" + valorDeposito + " depositado com sucesso.");
                            break;
                        }
                    }
                    break;
                case 5:
                    //saque
                    System.out.print("Digite CPF do cliente: ");
                    String cpfClienteSaque = scanner.nextLine();
                    Cliente clienteSaque = null;
                    for (Cliente cliente : banco.clientes) {
                        if (cliente.getCpf().equals(cpfClienteSaque)) {
                            clienteSaque = cliente;
                            break;
                        }
                    }
                    if (clienteSaque == null) {
                        System.out.println("Não encontrado.");
                        break;
                    }

                    System.out.print("Informe o valor do saque: ");
                    double valorSaque = scanner.nextDouble();

                    if (valorSaque <= 0) {
                        System.out.println("Impossível realizar o saque. Informe valor positivo.");
                        return;
                    }

                    for (Conta conta : banco.contas) {
                        if (conta.getCliente() == clienteSaque) {
                            conta.sacar(valorSaque);
                            System.out.println("Saque realizado com sucesso.");
                            break;
                        }
                    }
                    break;
                case 6:
                    //extrato
                    System.out.print("Digite CPF do cliente: ");
                    String cpfClienteExtrato = scanner.nextLine();
                    Cliente clienteExtrato = null;
                    for (Cliente cliente : banco.clientes) {
                        if (cliente.getCpf().equals(cpfClienteExtrato)) {
                            clienteExtrato = cliente;
                            break;
                        }
                    }
                    if (clienteExtrato == null) {
                        System.out.println("Não encontrado.");
                        break;
                    }

                    for (Conta conta : banco.contas) {
                        if (conta.getCliente() == clienteExtrato) {
                            List<String> extrato = banco.consultarExtrato(clienteExtrato, conta);
                            System.out.println("Extrato de " + clienteExtrato.getNome() + ":");
                            for (String transacao : extrato) {
                                System.out.println(transacao);
                            }
                            break;
                        }
                    }
                    break;
                case 7:
                    System.out.println("Saiu.");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                case 8:
                    while (true) {
                        System.out.println("\nAdministração do Banco:");
                        System.out.println("1. Saldo médio de contas");
                        System.out.println("2. Valor em custódia por tipo de conta");
                        System.out.println("3. Cliente com maior saldo");
                        System.out.println("4. Cliente com menor saldo");
                        System.out.println("5. Voltar ao menu principal");
                        System.out.print("Escolha uma opção: ");

                        int opcaoAdmin = scanner.nextInt();
                        scanner.nextLine();

                        switch (opcaoAdmin) {
                            case 1:
                                double saldoMedio = banco.calcularSaldoMedio();
                                System.out.println("Saldo médio das contas: R$ " + saldoMedio);
                                break;
                            case 2:
                                double totalCorrente = 0, totalPoupanca = 0, totalInvestimento = 0, totalRendaFixa = 0;

                                for (Conta conta : banco.contas) {
                                    double saldo = conta.consultarSaldo();
                                    if (conta instanceof ContaCorrente) {
                                        totalCorrente += saldo;
                                    } else if (conta instanceof ContaPoupanca) {
                                        totalPoupanca += saldo;
                                    } else if (conta instanceof ContaInvestimento) {
                                        totalInvestimento += saldo;
                                    } else if (conta instanceof ContaRendaFixa) {
                                        totalRendaFixa += saldo;
                                    }
                                }

                                System.out.println("Valor em custódia por tipo de conta:");
                                System.out.printf("Conta Corrente: R$ %.2f\n", totalCorrente);
                                System.out.printf("Conta Poupança: R$ %.2f\n", totalPoupanca);
                                System.out.printf("Conta Investimento: R$ %.2f\n", totalInvestimento);
                                System.out.printf("Conta Renda Fixa: R$ %.2f\n", totalRendaFixa);
                                break;
                            case 3:
                                Cliente clienteMaior = banco.encontrarClienteComMaiorSaldo();
                                if (clienteMaior != null) {
                                    System.out.println("Cliente com maior saldo: " + clienteMaior.getNome() + " - CPF: " + clienteMaior.getCpf());
                                } else {
                                    System.out.println("Nenhum cliente encontrado.");
                                }
                                break;
                            case 4:
                                Cliente clienteMenor = banco.encontrarClienteComMenorSaldo();
                                if (clienteMenor != null) {
                                    System.out.println("Cliente com menor saldo: " + clienteMenor.getNome() + " - CPF: " + clienteMenor.getCpf());
                                } else {
                                    System.out.println("Nenhum cliente encontrado.");
                                }
                                break;
                            case 5:
                                System.out.println("Retornando ao menu principal.");
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                        }

                        if (opcaoAdmin == 5) break;
                    }
                    break;
            }
        }
    }
}