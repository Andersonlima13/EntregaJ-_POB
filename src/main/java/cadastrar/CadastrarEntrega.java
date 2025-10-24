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

            // Garantir que a lista do entregador esteja inicializada
            if (entregador.getListaDeEntrega() == null) {
                entregador.setListaDeEntrega(new ArrayList<>());
            }

            // 🔹 Buscar pedidos existentes (por id)
            List<Pedido> pedidosEncontrados = new ArrayList<>();
            if (pedidosIds != null) {
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
            }

            // 🔹 Criar nova entrega e associar ao entregador
            Entrega entrega = new Entrega();
            entrega.setData(data);
            entrega.setLatitude(latitude);
            entrega.setLongitude(longitude);
            entrega.setEntregador(entregador);

            // Associar pedidos à entrega (e persistir cada pedido atualizado)
            for (Pedido p : pedidosEncontrados) {
                entrega.adicionarPedido(p); // também seta p.setEntrega(this)
                manager.store(p);
            }

            // 🔹 Associar entrega ao entregador (mantendo consistência)
            entregador.getListaDeEntrega().add(entrega);

            // 🔹 Persistir tudo (entrega e entregador)
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
        // Exemplo: informe um entregador e pedidos já existentes
        int entregadorId = 3;
        List<Integer> pedidosIds = List.of(3); // IDs reais dos pedidos

        new CadastrarEntrega("2025-10-04", -7.1186, -34.8811, entregadorId, pedidosIds);
    }
}
