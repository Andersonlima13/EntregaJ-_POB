package cadastrar;

import banco.Util;
import model.Pedido;
import com.db4o.ObjectContainer;

public class CadastrarPedido {
    private ObjectContainer manager;

    public CadastrarPedido() {
        manager = Util.conectarBanco();

        try {
            System.out.println("Cadastrando pedido...");

            Pedido pedido = new Pedido();
            pedido.setData("17/05/2025");
            pedido.setValor(20.00);
            pedido.setDescricao("Pedido fulanooo");

            manager.store(pedido);
            manager.commit();

            System.out.println("✅ Pedido cadastrado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar pedido: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new CadastrarPedido();
    }
}
