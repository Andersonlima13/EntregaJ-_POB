package repositorio;

import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import appconsole.Util;

public abstract class CRUDRepositorio<T> {
    protected ObjectContainer manager;

    public void conectar() {
        manager = Util.conectarBanco();
    }

    public void desconectar() {
        manager = null;
    }

    public void criar(T objeto) {
        manager.store(objeto);
    }

    public void atualizar(T objeto) {
        manager.store(objeto);
    }

    public abstract T ler(Object chave);

    public void apagar(T objeto) {
        manager.delete(objeto);
    }

    @SuppressWarnings("unchecked")
    public List<T> listar() {
        Class<T> type = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        Query q = manager.query();
        q.constrain(type);
        List<T> resultado = q.execute();
        return new ArrayList<>(resultado);
    }

    public void begin() {
        // db4o starts transactions automatically
    }

    public void commit() {
        if (manager != null)
            manager.commit();
    }

    public void rollback() {
        if (manager != null)
            manager.rollback();
    }
}
