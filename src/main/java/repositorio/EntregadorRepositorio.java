package repositorio;

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;

import model.Entregador;

public class EntregadorRepositorio extends CRUDRepositorio<Entregador> {

    @Override
    public Entregador ler(Object chave) {
        conectar();
        try {
            if (chave instanceof Integer) {
                int id = (Integer) chave;
                Query q = manager.query();
                q.constrain(Entregador.class);
                q.descend("id").constrain(id);
                List<Entregador> res = q.execute();
                return res.isEmpty() ? null : res.get(0);
            } else if (chave instanceof String) {
                String nome = (String) chave;
                Query q = manager.query();
                q.constrain(Entregador.class);
                q.descend("nome").constrain(nome);
                List<Entregador> res = q.execute();
                return res.isEmpty() ? null : res.get(0);
            }
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Entregador> listarPorNome(String texto) {
        conectar();
        try {
            Query q = manager.query();
            q.constrain(Entregador.class);
            q.descend("nome").constrain(texto);
            return q.execute();
        } finally {
            desconectar();
        }
    }
}
