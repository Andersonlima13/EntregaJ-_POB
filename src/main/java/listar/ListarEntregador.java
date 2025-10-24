package listar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entregador;
import model.Entrega;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarEntregador {
    private ObjectContainer manager;

    public ListarEntregador() {
        manager = Util.conectarBanco();

        try {
            System.out.println("Listando entregadores...\n");

            // Buscar todos os entregadores
            Query queryEntregadores = manager.query();
            queryEntregadores.constrain(Entregador.class);
            List<Entregador> entregadores = queryEntregadores.execute();

            if (entregadores.isEmpty()) {
                System.out.println("Nenhum entregador encontrado.");
                return;
            }

            // Buscar todas as entregas
            Query queryEntregas = manager.query();
            queryEntregas.constrain(Entrega.class);
            List<Entrega> entregas = queryEntregas.execute();

            // Mapear entregadorId -> total de entregas
            Map<Integer, Integer> totalPorEntregador = new HashMap<>();
            for (Entrega e : entregas) {
                if (e.getEntregador() != null) {
                    int idEntregador = e.getEntregador().getId(); // int agora
                    totalPorEntregador.put(
                            idEntregador,
                            totalPorEntregador.getOrDefault(idEntregador, 0) + 1
                    );
                }
            }

            // Exibir entregadores com o total de entregas calculado
            for (Entregador ent : entregadores) {
                int total = totalPorEntregador.getOrDefault(ent.getId(), 0);
                System.out.println("----------------------------------------");
                System.out.println("🧍 Entregador: " + ent.getNome());
                System.out.println("🆔 ID: " + ent.getId());
                System.out.println("📦 Total de entregas: " + total);
            }


        } catch (Exception e) {
            System.err.println("🚨 Erro ao listar entregadores: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("\n🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new ListarEntregador();
    }
}
