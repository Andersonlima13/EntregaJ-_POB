package appconsole;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Entregador;
import model.Pedido;
import java.util.List;

public class Listar {

    private ObjectContainer manager;

    public Listar() {
        manager = Util.conectarBanco();

        try {
            System.out.println("=== LISTANDO TODOS OS OBJETOS DO BANCO ===\n");

            // Listar Entregadores
            System.out.println("ENTREGADORES:");
            System.out.println("─".repeat(60));
            Query queryEntregadores = manager.query();
            queryEntregadores.constrain(Entregador.class);
            List<Entregador> entregadores = queryEntregadores.execute();

            if (entregadores.isEmpty()) {
                System.out.println("Nenhum entregador cadastrado.");
            } else {
                for (Entregador ent : entregadores) {
                    System.out.println("ID: " + ent.getId() + " | Nome: " + ent.getNome());
                    System.out.println("• Total de entregas: " + 
                        (ent.getListaDeEntrega() != null ? ent.getListaDeEntrega().size() : 0));
                    
                    if (ent.getListaDeEntrega() != null && !ent.getListaDeEntrega().isEmpty()) {
                        for (Entrega e : ent.getListaDeEntrega()) {
                            System.out.println("• Entrega #" + e.getId() + " em " + e.getData());
                        }
                    }
                    System.out.println();
                }
            }

            // Listar Entregas
            System.out.println("\n ENTREGAS:");
            System.out.println("─".repeat(60));
            Query queryEntregas = manager.query();
            queryEntregas.constrain(Entrega.class);
            List<Entrega> entregas = queryEntregas.execute();

            if (entregas.isEmpty()) {
                System.out.println("Nenhuma entrega cadastrada.");
            } else {
                for (Entrega e : entregas) {
                    System.out.println("ID: " + e.getId() + " | Data: " + e.getData());
                    System.out.println("  • Localização: [" + e.getLatitude() + ", " + e.getLongitude() + "]");
                    System.out.println("  • Entregador: " + 
                        (e.getEntregador() != null ? e.getEntregador().getNome() + " (ID: " + e.getEntregador().getId() + ")" : "N/A"));
                    System.out.println("  └─ Pedidos (" + e.getPedidos().size() + "):");
                    
                    for (Pedido p : e.getPedidos()) {
                        System.out.println("     • Pedido #" + p.getId() + " - " + p.getDescricao() + " (R$ " + p.getValor() + ")");
                    }
                    System.out.println();
                }
            }

            // Listar Pedidos
            System.out.println("\n PEDIDOS:");
            System.out.println("─".repeat(60));
            Query queryPedidos = manager.query();
            queryPedidos.constrain(Pedido.class);
            List<Pedido> pedidos = queryPedidos.execute();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido cadastrado.");
            } else {
                for (Pedido p : pedidos) {
                    System.out.println("ID: " + p.getId() + " | Data: " + p.getData());
                    System.out.println("  • Descrição: " + p.getDescricao());
                    System.out.println("  • Valor: R$ " + p.getValor());
                    System.out.println("  • Entrega: " + 
                        (p.getEntrega() != null ? "#" + p.getEntrega().getId() + " por " + p.getEntrega().getEntregador().getNome() : "Sem entrega"));
                    System.out.println();
                }
            }

            System.out.println("=== FIM DA LISTAGEM ===");

        } catch (Exception e) {
            System.err.println(" Erro ao listar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
            System.out.println("\n Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new Listar();
    }
}
