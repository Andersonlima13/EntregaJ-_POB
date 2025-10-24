package atualizar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;

import java.util.List;

public class AtualizarEntrega {
    private ObjectContainer manager;

    public AtualizarEntrega(int id, String novaData, Double novaLatitude, Double novaLongitude) {
        manager = Util.conectarBanco();

        try {
            System.out.println("🔍 Buscando entrega com ID: " + id);

            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("id").constrain(id);

            List<Entrega> resultados = query.execute();

            if (resultados.isEmpty()) {
                System.out.println("⚠️ Nenhuma entrega encontrada com o ID informado.");
            } else {
                Entrega entrega = resultados.get(0);

                if (novaData != null) entrega.setData(novaData);
                if (novaLatitude != null) entrega.setLatitude(novaLatitude);
                if (novaLongitude != null) entrega.setLongitude(novaLongitude);

                manager.store(entrega);
                manager.commit();

                System.out.println("✅ Entrega atualizada com sucesso!");
                System.out.println("Novo estado: " + entrega);
            }

        } catch (Exception e) {
            System.err.println("🚨 Erro ao atualizar entrega: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        // Exemplo de atualização por ID (int)
        new AtualizarEntrega(1, "2000-10-06", -7.11888, -34.861);
    }
}
