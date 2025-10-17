package cadastrar;

import banco.Util;
import com.db4o.ObjectContainer;
import model.Entregador;

public class CadastrarEntregador {
    private ObjectContainer manager;

    public CadastrarEntregador() {
        manager = Util.conectarBanco();

        try {
            System.out.println("Cadastrando entregador...");

            Entregador entregador = new Entregador("Maria Oliveira");

            manager.store(entregador);
            manager.commit();

            System.out.println("Entregador cadastrado com sucesso!");
            System.out.println(entregador); // usa o toString

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar entregador: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new CadastrarEntregador();
    }
}
