package cadastrar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Entregador;
import model.Pedido;

import java.util.ArrayList;
import java.util.List;

public class CadastrarEntrega {
    private ObjectContainer manager;

    public CadastrarEntrega(String data, double latitude, double longitude, int entregadorId, List<Integer> pedidosIds) {
        manager = Util.conectarBanco();

        try {
            System.out.println("🚚 Cadastrando nova entrega...");

            // 🔹 Buscar entregador existente pelo ID (int)
            Query queryEntregador = manager.query();
            queryEntregador.constrain(Entregador.class);
            queryEntregador.descend("id").constrain(entregadorId);
            List<Entregador> entregadores = queryEntregador.execute();

            if (entregadores.isEmpty()) {
                throw new Exception("Entregador com ID '" + entregadorId + "' não encontrado!");
            }

            Entregador entregador = entregadores.get(0);

            // 🔹 Buscar pedidos existentes
            List<Pedido> pedidosEncontrados = new ArrayList<>();
            for (int pid : pedidosIds) {
                Query queryPedido = manager.query();
                queryPedido.constrain(Pedido.class);
                queryPedido.descend("id").constrain(pid);
                List<Pedido> pedidos = queryPedido.execute();

                if (pedidos.isEmpty()) {
                    throw new Exception("Pedido com ID '" + pid + "' não encontrado!");
                }

                pedidosEncontrados.add(pedidos.get(0));
            }

            // 🔹 Criar nova entrega
            Entrega entrega = new Entrega();
            entrega.setData(data);
            entrega.setLatitude(latitude);
            entrega.setLongitude(longitude);
            entrega.setEntregador(entregador);

            // Associar pedidos à entrega
            for (Pedido p : pedidosEncontrados) {
                entrega.adicionarPedido(p);
                manager.store(p); // Atualiza o pedido no DB
            }

            // 🔹 Associar entrega ao entregador
            entregador.getListaDeEntrega().add(entrega);

            // 🔹 Persistir tudo
            manager.store(entrega);
            manager.store(entregador);
            manager.commit();

            System.out.println("✅ Entrega cadastrada com sucesso!");
            System.out.println(entrega);

        } catch (Exception e) {
            System.err.println("🚨 Erro ao cadastrar entrega: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        /*
         * Antes de rodar:
         * - Certifique-se de que já existam:
         *   → Um entregador cadastrado (use o ID dele)
         *   → Pedidos cadastrados (use seus IDs)
         */

        int entregadorId = 2; // exemplo: ID do entregador existente
        List<Integer> pedidosIds = List.of(1); // exemplo: IDs dos pedidos existentes

        new CadastrarEntrega("2025-10-04", -7.1186, -34.8811, entregadorId, pedidosIds);
    }
}
