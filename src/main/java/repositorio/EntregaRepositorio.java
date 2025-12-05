package repositorio;

import java.util.List;

import com.db4o.query.Query;

import model.Entrega;

public class EntregaRepositorio extends CRUDRepositorio<Entrega> {

    @Override
    public Entrega ler(Object chave) {
        conectar();
        try {
            if (chave instanceof Integer) {
                int id = (Integer) chave;
                Query q = manager.query();
                q.constrain(Entrega.class);
                q.descend("id").constrain(id);
                List<Entrega> res = q.execute();
                return res.isEmpty() ? null : res.get(0);
            }
            return null;
        } finally {
            desconectar();
        }
    }

    public List<Entrega> listarPorEntregadorId(int entregadorId) {
        conectar();
        try {
            Query q = manager.query();
            q.constrain(Entrega.class);
            q.descend("entregador").descend("id").constrain(entregadorId);
            return q.execute();
        } finally {
            desconectar();
        }
    }
}
