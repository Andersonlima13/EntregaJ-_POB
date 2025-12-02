package appconsole;

import requisito.Fachada;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Entregador;
import model.Pedido;

import java.util.List;

public class Consultar {
    private ObjectContainer manager;

    public Consultar() {
        manager = Util.conectarBanco();
    }


     // CONSULTA 1: Quais os pedidos sem entrega

    public void pedidosSemEntrega() {
        try {
            System.out.println("\nCONSULTA 1: Pedidos sem entrega");
            System.out.println("─".repeat(60));

            Query query = manager.query();
            query.constrain(Pedido.class);
            query.descend("entrega").constrain(null);

            List<Pedido> pedidos = query.execute();

            if (pedidos.isEmpty()) {
                System.out.println("Todos os pedidos têm entrega associada.");
            } else {
                System.out.println("Total de pedidos sem entrega: " + pedidos.size() + "\n");

                for (Pedido p : pedidos) {
                    System.out.println("Pedido #" + p.getId());
                    System.out.println("   Data: " + p.getData());
                    System.out.println("   Descrição: " + p.getDescricao());
                    System.out.println("   Valor: R$ " + p.getValor());
                    System.out.println("   Status: SEM ENTREGA");
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("Erro na consulta 1: " + e.getMessage());
        }
    }

//CONSULTA 2: Quais os pedidos entregues pelo entregador de nome X
    public void pedidosPorEntregador(String nomeEntregador) {
        try {
            System.out.println("\nCONSULTA 2: Pedidos entregues por " + nomeEntregador);
            System.out.println("─".repeat(60));

            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("entregador").descend("nome").constrain(nomeEntregador);

            List<Entrega> entregas = query.execute();

            if (entregas.isEmpty()) {
                System.out.println("Nenhuma entrega encontrada para o entregador: " + nomeEntregador);
            } else {
                int totalPedidos = 0;
                System.out.println("Entregas realizadas: " + entregas.size() + "\n");

                for (Entrega e : entregas) {
                    System.out.println("Entrega #" + e.getId() + " (" + e.getData() + "):");

                    for (Pedido p : e.getPedidos()) {
                        totalPedidos++;
                        System.out.println("    Pedido #" + p.getId() + " - " + p.getDescricao());
                        System.out.println("    Data: " + p.getData() + " | Valor: R$ " + p.getValor());
                    }
                    System.out.println();
                }

                System.out.println("Total de pedidos entregues por " + nomeEntregador + ": " + totalPedidos);
            }

        } catch (Exception e) {
            System.err.println("Erro na consulta 2: " + e.getMessage());
        }
    }

    
     //CONSULTA 3: Quais os entregadores que têm mais de N entregas
    public void entregadoresComMaisDeNEntregas(int n) {
        try {
            System.out.println("\nCONSULTA 3: Entregadores com mais de " + n + " entrega(s)");
            System.out.println("─".repeat(60));

            Query query = manager.query();
            query.constrain(Entregador.class);

            List<Entregador> entregadores = query.execute();

            if (entregadores.isEmpty()) {
                System.out.println("Nenhum entregador cadastrado no banco.");
                return;
            }

            boolean encontrou = false;

            for (Entregador ent : entregadores) {
                if (ent.getListaDeEntrega() == null) {
                    ent.setListaDeEntrega(new java.util.ArrayList<>());
                }

                int qtdEntregas = ent.getListaDeEntrega().size();

                if (qtdEntregas > n) {
                    encontrou = true;
                    System.out.println(ent.getNome() + " (ID: " + ent.getId() + ")");
                    System.out.println(" Total de entregas: " + qtdEntregas);
                    System.out.println(" Detalhes das entregas:");

                    for (Entrega e : ent.getListaDeEntrega()) {
                        System.out.println("     • Entrega #" + e.getId() +
                                " em " + e.getData() +
                                " (" + e.getPedidos().size() + " pedido(s))");
                    }
                    System.out.println();
                }
            }

            if (!encontrou) {
                System.out.println("Nenhum entregador com mais de " + n + " entrega(s).");
            }

        } catch (Exception e) {
            System.err.println("Erro na consulta 3: " + e.getMessage());
        }
    }

    
    public void fechar() {
        Util.desconectar();
    }

    public static void main(String[] args) {
        Consultar consultar = new Consultar();

        try {
            // CONSULTA 1: Pedidos sem entrega
            consultar.pedidosSemEntrega();

            // CONSULTA 2: Pedidos entregues por um entregador específico
            consultar.pedidosPorEntregador("João Silva");

            // CONSULTA 3: Entregadores com mais de N entrega
            consultar.entregadoresComMaisDeNEntregas(1);

            System.out.println("\n" + "=".repeat(60));
            System.out.println(" Todas as consultas foram executadas!");
            System.out.println("=".repeat(60));
        } 
        
        finally {
            consultar.fechar();
            System.out.println("Conexão encerrada.");
        }
    }
}