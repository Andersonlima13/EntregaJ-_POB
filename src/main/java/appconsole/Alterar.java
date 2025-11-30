package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Pedido;

import java.util.List;

public class Alterar {
    private ObjectContainer manager;

    public Alterar(int idPedido) {
        manager = Util.conectarBanco();

        try {
            System.out.println("=== REMOVENDO RELACIONAMENTO PEDIDO-ENTREGA ===\n");
            System.out.println("Buscando pedido com ID: " + idPedido);

            // Buscar o pedido pelo ID
            Query queryPedido = manager.query();
            queryPedido.constrain(Pedido.class);
            queryPedido.descend("id").constrain(idPedido);
            List<Pedido> resultados = queryPedido.execute();

            if (resultados.isEmpty()) {
                System.out.println(" Pedido não encontrado.");
                return;
            }

            Pedido pedido = resultados.get(0);
            Entrega entregaAnterior = pedido.getEntrega();

            if (entregaAnterior == null) {
                System.out.println("Este pedido não possui entrega associada.");
                System.out.println(pedido);
                return;
            }

            System.out.println("\n Estado ANTES da remoção:");
            System.out.println(" • Pedido: " + pedido);
            System.out.println(" • Entrega associada: #" + entregaAnterior.getId() + 
                             " (" + entregaAnterior.getData() + ")");
            System.out.println(" • Entregador: " + entregaAnterior.getEntregador().getNome());

            // Remover o pedido da lista de pedidos da entrega
            boolean removido = entregaAnterior.getPedidos().remove(pedido);
            
            if (removido) {
                pedido.setEntrega(null);

                manager.store(entregaAnterior);
                manager.store(pedido);
                manager.commit();

                System.out.println("\n Relacionamento removido com sucesso!");
                System.out.println("\n Estado DEPOIS da remoção:");
                System.out.println(" • Pedido: " + pedido);
                System.out.println(" • Entrega #" + entregaAnterior.getId() + 
                                 " agora tem " + entregaAnterior.getPedidos().size() + " pedido(s)");
            } else {
                System.out.println("O pedido não estava na lista da entrega.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao remover relacionamento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
            System.out.println("\n Conexão encerrada.");
        }
    }

    public static void removerEntregadorDeEntrega(int idEntrega) {
        ObjectContainer manager = Util.conectarBanco();

        try {
            System.out.println("=== REMOVENDO RELACIONAMENTO ENTREGADOR-ENTREGA ===\n");
            System.out.println("Buscando entrega com ID: " + idEntrega);

            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("id").constrain(idEntrega);
            List<Entrega> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("Entrega não encontrada.");
                return;
            }

            Entrega entrega = resultados.get(0);
            
            if (entrega.getEntregador() == null) {
                System.out.println("Esta entrega não possui entregador associado.");
                return;
            }

            System.out.println("\n Estado ANTES:");
            System.out.println(" • Entrega: #" + entrega.getId());
            System.out.println(" • Entregador: " + entrega.getEntregador().getNome());

            // Remover da lista do entregador
            entrega.getEntregador().getListaDeEntrega().remove(entrega);
            manager.store(entrega.getEntregador()); 

            manager.commit();
            
            System.out.println("\n Relacionamento removido!");

        } catch (Exception e) {
            System.err.println(" Erro: " + e.getMessage());
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new Alterar(2);
        
    }
}