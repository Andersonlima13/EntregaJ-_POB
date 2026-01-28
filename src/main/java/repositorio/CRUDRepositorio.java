package repositorio;

import java.util.List;
import jakarta.persistence.EntityManager;
import util.Util;

public abstract class CRUDRepositorio<T> {

    protected EntityManager manager;

    public void conectar() {
        manager = Util.conectar();
    }

    public void desconectar() {
        Util.desconectar();
    }

    public void criar(T obj) {
        manager.persist(obj);
    }

    public void atualizar(T obj) {
        manager.merge(obj);
    }

    public void apagar(T obj) {
        manager.remove(obj);
    }

    public abstract T ler(Object chave);
    public abstract List<T> listar();

    // 🔹 Controle transacional padrão antigo
    public static void begin() {
        if (!Util.getManager().getTransaction().isActive())
            Util.getManager().getTransaction().begin();
    }

    public static void commit() {
        if (Util.getManager().getTransaction().isActive()) {
            Util.getManager().getTransaction().commit();
            Util.getManager().clear();
        }
    }

    public static void rollback() {
        if (Util.getManager().getTransaction().isActive())
            Util.getManager().getTransaction().rollback();
    }
}
