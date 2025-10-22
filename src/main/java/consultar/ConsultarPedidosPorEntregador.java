package consultar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Entregador;
import model.Pedido;

import java.util.List;

public class ConsultarPedidosPorEntregador {
    private ObjectContainer manager;

    public ConsultarPedidosPorEntregador(String nomeEntregador) {
        manager = Util.conectarBanco();

        try {
            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("entregador").descend("nome").constrain(nomeEntregador);

            List<Entrega> entregas = query.execute();

            if (entregas.isEmpty()) {
                System.out.println("❌ Nenhuma entrega encontrada para o entregador: " + nomeEntregador);
            } else {
                System.out.println("📦 Pedidos entregues por " + nomeEntregador + ":");
                for (Entrega entrega : entregas) {
                    for (Pedido pedido : entrega.getPedidos()) {
                        System.out.println(pedido);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar pedidos por entregador: " + e.getMessage());
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new ConsultarPedidosPorEntregador("Carlos");
    }
}
