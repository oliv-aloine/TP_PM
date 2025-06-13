package banco;

import java.util.List;

public interface Conta {
    double consultarSaldo();

    List<String> consultarExtrato();

    void sacar(double valor);

    void depositar(double valor);

    Cliente getCliente();

    TipoConta getTipoConta();
}