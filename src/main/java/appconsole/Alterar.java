package appconsole;

import jakarta.persistence.EntityManager;
import model.Entrega;
import model.Pedido;

public class Alterar {

    private EntityManager manager;

    public Alterar(int idPedido) {
        manager = Util.conectarBanco();

        try {
            manager.getTransaction().begin();

            Pedido pedido = manager.find(Pedido.class, idPedido);

            if (pedido == null) {
                System.out.println("Pedido não encontrado.");
                return;
            }

            Entrega entrega = pedido.getEntrega();

            if (entrega == null) {
                System.out.println("Pedido não possui entrega.");
                return;
            }

            entrega.getPedidos().remove(pedido);
            pedido.setEntrega(null);

            manager.merge(entrega);
            manager.merge(pedido);

            manager.getTransaction().commit();

            System.out.println("Relacionamento Pedido–Entrega removido com sucesso.");

        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            Util.desconectar();
        }
    }

    public static void removerEntregadorDeEntrega(int idEntrega) {
        EntityManager manager = Util.conectarBanco();

        try {
            manager.getTransaction().begin();

            Entrega entrega = manager.find(Entrega.class, idEntrega);

            if (entrega == null || entrega.getEntregador() == null) {
                System.out.println("Entrega inválida.");
                return;
            }

            entrega.getEntregador().getListaDeEntrega().remove(entrega);
            entrega.setEntregador(null);

            manager.merge(entrega);

            manager.getTransaction().commit();
            System.out.println("Entregador removido da entrega.");

        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new Alterar(2);
    }
}
