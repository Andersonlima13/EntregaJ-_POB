package deletar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entregador;

import java.util.List;

public class DeletarEntregador {
    private ObjectContainer manager;

    public DeletarEntregador(String idEntregador) {
        manager = Util.conectarBanco();

        try {
            System.out.println("🗑️ Deletando entregador com ID: " + idEntregador);

            Query query = manager.query();
            query.constrain(Entregador.class);
            query.descend("id").constrain(idEntregador);

            List<Entregador> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("⚠️ Nenhum entregador encontrado com o ID informado.");
            } else {
                for (Entregador e : resultados) {
                    manager.delete(e);
                    System.out.println("✅ Entregador removido: " + e);
                }
                manager.commit();
            }

        } catch (Exception e) {
            System.err.println("🚨 Erro ao deletar entregador: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new DeletarEntregador("7764");
    }
}
