package repositorio;

import java.util.List;

import com.db4o.query.Query;

import model.Pedido;

public class PedidoRepositorio extends CRUDRepositorio<Pedido> {

    @Override
    public Pedido ler(Object chave) {
        conectar();
        try {
            if (chave instanceof Integer) {
                int id = (Integer) chave;
                Query q = manager.query();
                q.constrain(Pedido.class);
                q.descend("id").constrain(id);
                List<Pedido> res = q.execute();
                return res.isEmpty() ? null : res.get(0);
            }
            return null;
        } finally {
            desconectar();
        }
    }
}
