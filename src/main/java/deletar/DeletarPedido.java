package deletar;

import banco.Util;
import model.Pedido;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import java.util.List;

public class DeletarPedido {
    private ObjectContainer manager;

    public DeletarPedido(String idParaDeletar) {
        manager = Util.conectarBanco();

        try {
            System.out.println("Buscando pedido para deletar...");

            // 🔍 Busca o pedido pelo ID
            Query query = manager.query();
            query.constrain(Pedido.class);
            query.descend("id").constrain(idParaDeletar);
            List<Pedido> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("Pedido com ID " + idParaDeletar + " não encontrado.");
            } else {
                Pedido pedido = resultados.get(0);
                manager.delete(pedido);
                manager.commit();

                System.out.println("Pedido deletado com sucesso!");
                System.out.println(pedido);
            }

        } catch (Exception e) {
            System.err.println("Erro ao deletar pedido: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("Conexão encerrada");
        }
    }

    public static void main(String[] args) {
        //  instancias para deletar o pedido com ID específico
        new DeletarPedido("coloque_aqui_o_uuid_do_pedido");
    }
}
