package atualizar;

import banco.Util;
import model.Pedido;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import java.util.List;

public class AtualizarPedido {
    private ObjectContainer manager;

    public AtualizarPedido(int idParaAtualizar, String novaDescricao, double novoValor) {
        manager = Util.conectarBanco();

        try {
            System.out.println("Buscando pedido para atualizar...");

            // Busca o pedido pelo ID
            Query query = manager.query();
            query.constrain(Pedido.class);
            query.descend("id").constrain(idParaAtualizar);
            List<Pedido> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("Pedido com ID " + idParaAtualizar + " não encontrado.");
            } else {
                Pedido pedido = resultados.get(0);

                // Atualiza os campos desejados
                pedido.setDescricao(novaDescricao);
                pedido.setValor(novoValor);

                manager.store(pedido);
                manager.commit();

                System.out.println("Pedido atualizado com sucesso!");
                System.out.println(pedido);
            }

        } catch (Exception e) {
            System.err.println("Erro ao atualizar pedido: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new AtualizarPedido(2, "Pedido atualizado 2", 35.50);
    }
}
