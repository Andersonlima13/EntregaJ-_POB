package repositorio;

import model.Entrega;
import util.Util;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EntregaRepositorio extends CRUDRepositorio<Entrega> {

    @Override
    public Entrega ler(Object chave) {
        if (chave instanceof Integer) {
            return Util.getManager().find(Entrega.class, (Integer) chave);
        }
        return null;
    }

    @Override
    public List<Entrega> listar() {
        String jpql = "SELECT e FROM Entrega e ORDER BY e.data DESC";
        TypedQuery<Entrega> query = Util.getManager().createQuery(jpql, Entrega.class);
        return query.getResultList();
    }

    /**
     * CONSULTA 2: Pedidos entregues pelo entregador de nome X 
     * Retorna todas as entregas do entregador com seus pedidos
     */
    public List<Entrega> buscarPorNomeEntregador(String nomeEntregador) {
        String jpql = "SELECT DISTINCT e FROM Entrega e " +
                      "LEFT JOIN FETCH e.pedidos " +
                      "WHERE e.entregador.nome = :nome";
        
        TypedQuery<Entrega> query = Util.getManager().createQuery(jpql, Entrega.class);
        query.setParameter("nome", nomeEntregador);
        
        return query.getResultList();
    }
}