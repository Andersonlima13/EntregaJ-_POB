package repositorio;

import jakarta.persistence.EntityManager;
import java.util.List;

public abstract class CRUDRepositorio<T> {

    protected EntityManager manager;

    public void setEntityManager(EntityManager manager) {
        this.manager = manager;
    }

    public void criar(T obj) {
        manager.persist(obj);
    }

    public void atualizar(T obj) {
        manager.merge(obj);
    }

    public void apagar(T obj) {
        manager.remove(manager.contains(obj) ? obj : manager.merge(obj));
    }

    public abstract T ler(Object chave);

    public abstract List<T> listar();
}
