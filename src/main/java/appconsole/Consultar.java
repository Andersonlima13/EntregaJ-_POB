package appconsole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Entrega;
import model.Entregador;
import model.Pedido;

import java.util.List;

public class Consultar {

    private EntityManager manager;

    public Consultar() {
        manager = Util.conectarBanco();
    }

    // CONSULTA 1: Pedidos sem entrega
    public void pedidosSemEntrega() {
        System.out.println("\nCONSULTA 1: Pedidos sem entrega");
        System.out.println("─".repeat(60));

        TypedQuery<Pedido> query =
                manager.createQuery(
                        "SELECT p FROM Pedido p WHERE p.entrega IS NULL",
                        Pedido.class
                );

        List<Pedido> pedidos = query.getResultList();

        if (pedidos.isEmpty()) {
            System.out.println("Todos os pedidos têm entrega associada.");
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println("Pedido #" + p.getId());
            System.out.println(" Data: " + p.getData());
            System.out.println(" Descrição: " + p.getDescricao());
            System.out.println(" Valor: R$ " + p.getValor());
            System.out.println(" Status: SEM ENTREGA\n");
        }
    }

    // CONSULTA 2: Pedidos entregues por entregador X
    public void pedidosPorEntregador(String nomeEntregador) {
        System.out.println("\nCONSULTA 2: Pedidos entregues por " + nomeEntregador);
        System.out.println("─".repeat(60));

        TypedQuery<Entrega> query =
                manager.createQuery(
                        "SELECT DISTINCT e FROM Entrega e " +
                        "LEFT JOIN FETCH e.pedidos " +
                        "WHERE e.entregador.nome = :nome",
                        Entrega.class
                );

        query.setParameter("nome", nomeEntregador);

        List<Entrega> entregas = query.getResultList();

        if (entregas.isEmpty()) {
            System.out.println("Nenhuma entrega encontrada.");
            return;
        }

        for (Entrega e : entregas) {
            System.out.println("Entrega #" + e.getId() + " (" + e.getData() + ")");
            for (Pedido p : e.getPedidos()) {
                System.out.println("  Pedido #" + p.getId() + " - " + p.getDescricao());
            }
            System.out.println();
        }
    }

    // CONSULTA 3: Entregadores com mais de N entregas
    public void entregadoresComMaisDeNEntregas(int n) {
        System.out.println("\nCONSULTA 3: Entregadores com mais de " + n + " entrega(s)");
        System.out.println("─".repeat(60));

        TypedQuery<Entregador> query =
                manager.createQuery(
                        "SELECT e FROM Entregador e " +
                        "WHERE SIZE(e.listaDeEntrega) > :n",
                        Entregador.class
                );

        query.setParameter("n", n);

        List<Entregador> entregadores = query.getResultList();

        if (entregadores.isEmpty()) {
            System.out.println("Nenhum entregador encontrado.");
            return;
        }

        for (Entregador e : entregadores) {
            System.out.println(e.getNome() +
                    " | Total de entregas: " + e.getListaDeEntrega().size());
        }
    }

    public void fechar() {
        Util.desconectar();
    }

    public static void main(String[] args) {
        Consultar c = new Consultar();
        c.pedidosSemEntrega();
        c.pedidosPorEntregador("João Silva");
        c.entregadoresComMaisDeNEntregas(1);
        c.fechar();
    }
}
