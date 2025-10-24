package cadastrar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Pedido;

import java.util.List;

public class CadastrarPedido {
    private ObjectContainer manager;

    public CadastrarPedido(String entregaId) {
        manager = Util.conectarBanco();

        try {
            System.out.println("Cadastrando pedido...");

            Pedido pedido = new Pedido();
            pedido.setData("17/05/2025");
            pedido.setValor(20.00);
            pedido.setDescricao("Pedido 2 -testando id");

            if (entregaId != null && !entregaId.isEmpty()) {
                Query queryEntrega = manager.query();
                queryEntrega.constrain(Entrega.class);
                queryEntrega.descend("id").constrain(entregaId);
                List<Entrega> entregas = queryEntrega.execute();

                if (entregas.isEmpty()) {
                    throw new Exception("Entrega com ID '" + entregaId + "' não encontrada!");
                }

                Entrega entrega = entregas.get(0);
                entrega.adicionarPedido(pedido);
                manager.store(entrega); // atualiza a entrega no banco
            }

            //  Salvar o pedido no banco
            manager.store(pedido);
            manager.commit();

            System.out.println("Pedido cadastrado com sucesso!");
            System.out.println(pedido);

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar pedido: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        // Caso queira cadastrar o pedido sem entrega:
        new CadastrarPedido(null);

        // Caso queira associar a uma entrega existente:
        // String entregaId = "e123";
        //new CadastrarPedido(entregaId);
    }
}
