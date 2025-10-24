package deletar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;
import model.Entregador;

import java.util.List;

public class DeletarEntrega {
    private ObjectContainer manager;

    public DeletarEntrega(int id) {
        manager = Util.conectarBanco();

        try {
            System.out.println("🗑️ Buscando entrega com ID: " + id);

            // Buscar entrega pelo ID
            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("id").constrain(id);
            List<Entrega> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("❌ Nenhuma entrega encontrada com o ID informado.");
                return;
            }

            Entrega entrega = resultados.get(0);

            // Remover referência no entregador (se existir)
            Entregador entregador = entrega.getEntregador();
            if (entregador != null) {
                // garantir lista inicializada
                if (entregador.getListaDeEntrega() != null) {
                    boolean removed = entregador.getListaDeEntrega().removeIf(e -> e.getId() == entrega.getId());
                    if (removed) {
                        manager.store(entregador); // atualizar entregador no banco
                    }
                }
            }

            // Deletar a entrega
            manager.delete(entrega);
            manager.commit();

            System.out.println("✅ Entrega removida com sucesso! ID: " + id);
            System.out.println("Entrega deletada: " + entrega);

        } catch (Exception e) {
            System.err.println("🚨 Erro ao deletar entrega: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        // exemplo: deletar entrega com id 1
        new DeletarEntrega(5);
    }
}
