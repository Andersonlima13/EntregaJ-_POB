package deletar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;

import java.util.List;

public class DeletarEntrega {
    private ObjectContainer manager;

    public DeletarEntrega(String id) {
        manager = Util.conectarBanco();

        try {
            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("id").constrain(id);

            List<Entrega> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("❌ Nenhuma entrega encontrada com o ID informado.");
            } else {
                Entrega entrega = resultados.get(0);
                manager.delete(entrega);
                manager.commit();

                System.out.println("🗑️ Entrega removida com sucesso! ID: " + id);
            }

        } catch (Exception e) {
            System.err.println("Erro ao deletar entrega: " + e.getMessage());
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new DeletarEntrega("7764");
    }
}
