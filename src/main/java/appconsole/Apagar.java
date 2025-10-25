package appconsole;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import model.Entrega;
import model.Entregador;
import model.Pedido;

public class Apagar {
    private ObjectContainer manager;

     //Apaga uma entrega e remove todas as referências
    public Apagar(int idEntrega) {
        manager = Util.conectarBanco();

        try {
            System.out.println("=== APAGANDO OBJETO ===\n");
            System.out.println("Buscando entrega com ID: " + idEntrega);

            // Buscar a entrega
            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("id").constrain(idEntrega);
            List<Entrega> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("Entrega não encontrada.");
                return;
            }

            Entrega entrega = resultados.get(0);

            System.out.println("\n Entrega encontrada:");
            System.out.println(" • ID: " + entrega.getId());
            System.out.println(" • Data: " + entrega.getData());
            System.out.println(" • Entregador: " + 
                (entrega.getEntregador() != null ? entrega.getEntregador().getNome() : "N/A"));
            System.out.println(" • Total de pedidos: " + entrega.getPedidos().size());

            // Remover referência no Entregador
            if (entrega.getEntregador() != null) {
                Entregador entregador = entrega.getEntregador();
                
                if (entregador.getListaDeEntrega() != null) {
                    boolean removido = entregador.getListaDeEntrega().removeIf(
                        e -> e.getId() == entrega.getId()
                    );
                    
                    if (removido) {
                        System.out.println("\nRemovendo referência no entregador '" + 
                            entregador.getNome() + "'...");
                        manager.store(entregador);
                    }
                }
            }

            // Remover referência nos Pedidos
            if (!entrega.getPedidos().isEmpty()) {
                System.out.println("Removendo referências em " + 
                    entrega.getPedidos().size() + " pedido(s)...");
                
                for (Pedido pedido : entrega.getPedidos()) {
                    pedido.setEntrega(null);
                    manager.store(pedido);
                    System.out.println("  • Pedido #" + pedido.getId() + 
                        " agora está sem entrega");
                }
            }

            //Deletar a Entrega
            manager.delete(entrega);
            manager.commit();

            System.out.println("\nEntrega #" + idEntrega + " apagada com sucesso!");
            System.out.println("Todos os relacionamentos foram removidos.");

        } catch (Exception e) {
            System.err.println("Erro ao apagar entrega: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
            System.out.println("\nConexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new Apagar(1);
    }
}