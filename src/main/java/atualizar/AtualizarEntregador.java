package atualizar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entregador;

import java.util.List;

public class AtualizarEntregador {
    private ObjectContainer manager;

    public AtualizarEntregador(String idEntregador, String novoNome) {
        manager = Util.conectarBanco();

        try {
            System.out.println("🔍 Buscando entregador com ID: " + idEntregador);

            Query query = manager.query();
            query.constrain(Entregador.class);
            query.descend("id").constrain(idEntregador);

            List<Entregador> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("⚠️ Nenhum entregador encontrado com o ID informado.");
            } else {
                Entregador entregador = resultados.get(0);
                System.out.println("📦 Entregador encontrado: " + entregador);

                entregador.setNome(novoNome);

                manager.store(entregador);
                manager.commit();

                System.out.println("✅ Entregador atualizado com sucesso!");
                System.out.println("🆕 Novo estado: " + entregador);
            }

        } catch (Exception e) {
            System.err.println("Erro ao atualizar entregador: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new AtualizarEntregador("fa5e", "Anderson sousa");
    }
}
