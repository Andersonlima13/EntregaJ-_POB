package repositorio;

import model.Pedido;
import util.Util;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PedidoRepositorio extends CRUDRepositorio<Pedido> {

    @Override
    public Pedido ler(Object chave) {
        if (chave instanceof Integer) {
            return Util.getManager().find(Pedido.class, (Integer) chave);
        }
        return null;
    }

    @Override
    public List<Pedido> listar() {
        String jpql = "SELECT p FROM Pedido p ORDER BY p.id";
        TypedQuery<Pedido> query = Util.getManager().createQuery(jpql, Pedido.class);
        return query.getResultList();
    }

    /**
     * CONSULTA 1: Pedidos SEM entrega 
     */
    public List<Pedido> buscarPedidosSemEntrega() {
        String jpql = "SELECT p FROM Pedido p WHERE p.entrega IS NULL";
        TypedQuery<Pedido> query = Util.getManager().createQuery(jpql, Pedido.class);
        return query.getResultList();
    }

}