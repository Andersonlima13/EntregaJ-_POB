package repositorio;

import model.Entrega;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EntregaRepositorio extends CRUDRepositorio<Entrega> {

    @Override
    public Entrega ler(Object chave) {
        if (chave instanceof Integer) {
            return manager.find(Entrega.class, (Integer) chave);
        }
        return null;
    }

    @Override
    public List<Entrega> listar() {
        String jpql = "SELECT e FROM Entrega e ORDER BY e.data DESC";
        TypedQuery<Entrega> query =
                manager.createQuery(jpql, Entrega.class);
        return query.getResultList();
    }

    public List<Entrega> buscarPorNomeEntregador(String nomeEntregador) {
        String jpql =
                "SELECT DISTINCT e FROM Entrega e " +
                "LEFT JOIN FETCH e.pedidos " +
                "WHERE e.entregador.nome = :nome";

        TypedQuery<Entrega> query =
                manager.createQuery(jpql, Entrega.class);
        query.setParameter("nome", nomeEntregador);

        return query.getResultList();
    }
}
