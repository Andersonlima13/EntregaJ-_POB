package listar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Pedido;

import java.util.List;

public class ListarPedido {
    private ObjectContainer manager;

    public ListarPedido() {
        manager = Util.conectarBanco();

        System.out.println("Listando pedidos...\n");

        // Cria uma query para buscar todos os objetos do tipo Pedido
        Query query = manager.query();
        query.constrain(Pedido.class);
        List<Pedido> resultados = query.execute();

        // Exibe os resultados
        if (resultados.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
        } else {
            for (Pedido pedido : resultados) {
                System.out.println(pedido.toString());
            }
        }

        Util.desconectar();
        System.out.println("\nFim da listagem.");
    }

    public static void main(String[] args) {
        new ListarPedido();
    }
}
