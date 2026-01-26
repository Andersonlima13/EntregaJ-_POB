package repositorio;

import model.Entregador;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EntregadorRepositorio extends CRUDRepositorio<Entregador> {

    @Override
    public Entregador ler(Object chave) {
        if (chave instanceof Integer) {
            return manager.find(Entregador.class, (Integer) chave);
        } 
        if (chave instanceof String) {
            return buscarPorNome((String) chave);
        }
        return null;
    }

    @Override
    public List<Entregador> listar() {
        String jpql = "SELECT e FROM Entregador e ORDER BY e.nome";
        TypedQuery<Entregador> query =
                manager.createQuery(jpql, Entregador.class);
        return query.getResultList();
    }

    public Entregador buscarPorNome(String nome) {
        String jpql = "SELECT e FROM Entregador e WHERE e.nome = :nome";
        TypedQuery<Entregador> query =
                manager.createQuery(jpql, Entregador.class);
        query.setParameter("nome", nome);

        List<Entregador> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public List<Entregador> buscarComMaisDeNEntregas(int n) {
        String jpql =
                "SELECT e FROM Entregador e " +
                "WHERE SIZE(e.listaDeEntrega) > :n " +
                "ORDER BY SIZE(e.listaDeEntrega) DESC";

        TypedQuery<Entregador> query =
                manager.createQuery(jpql, Entregador.class);
        query.setParameter("n", n);

        return query.getResultList();
    }
}
