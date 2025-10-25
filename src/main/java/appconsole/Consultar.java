package appconsole;

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

    public void entregasNaData(String data) {
        try {
            System.out.println("\n=== CONSULTA 1: Entregas na data " + data + " ===");
            System.out.println("─".repeat(60));

            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("data").constrain(data);

            List<Entrega> entregas = query.execute();

            if (entregas.isEmpty()) {
                System.out.println("Nenhuma entrega encontrada na data: " + data);
            } else {
                System.out.println("Total encontrado: " + entregas.size() + " entrega(s)\n");
                
                for (Entrega e : entregas) {
                    System.out.println("Entrega #" + e.getId());
                    System.out.println(" •  Data: " + e.getData());
                    System.out.println(" •  Localização: [" + e.getLatitude() + ", " + e.getLongitude() + "]");
                    System.out.println(" •  Entregador: " + 
                        (e.getEntregador() != null ? e.getEntregador().getNome() : "N/A"));
                    System.out.println(" •  Pedidos: " + e.getPedidos().size());
                    
                    for (Pedido p : e.getPedidos()) {
                        System.out.println("      " + p.getDescricao() + " (R$ " + p.getValor() + ")");
                    }
                    System.out.println();
                }
            }

        } catch (Exception e) {
            System.err.println("Erro na consulta 1: " + e.getMessage());
        }
    }

    public void pedidosPorEntregador(String nomeEntregador) {
        try {
            System.out.println("\n=== CONSULTA 2: Pedidos entregues por " + nomeEntregador + " ===");
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
                        System.out.println("  • Pedido #" + p.getId() + " - " + p.getDescricao());
                        System.out.println("    Data: " + p.getData() + " | Valor: R$ " + p.getValor());
                    }
                    System.out.println();
                }

                System.out.println("Total de pedidos entregues: " + totalPedidos);
            }

        } catch (Exception e) {
            System.err.println("Erro na consulta 2: " + e.getMessage());
        }
    }

    public void entregadoresComMaisDeNEntregas(int n) {
        try {
            System.out.println("\n=== CONSULTA 3: Entregadores com mais de " + n + " entrega(s) ===");
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
                    System.out.println("Total de entregas: " + qtdEntregas);
                    System.out.println("Entregas:");

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
            // CONSULTA 1: Entregas na data específica
            consultar.entregasNaData("2025-01-15");

            // CONSULTA 2: Pedidos entregues por um entregador específico
            consultar.pedidosPorEntregador("João Silva");

            // CONSULTA 3: Entregadores com mais de 1 entrega
            consultar.entregadoresComMaisDeNEntregas(2);

        } finally {
            consultar.fechar();
            System.out.println("Conexão encerrada.");
        }
    }
}