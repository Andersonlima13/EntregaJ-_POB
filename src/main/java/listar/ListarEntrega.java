package listar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;

import java.util.List;

public class ListarEntrega {
    private ObjectContainer manager;

    public ListarEntrega() {
        manager = Util.conectarBanco();

        try {
            Query query = manager.query();
            query.constrain(Entrega.class);

            List<Entrega> entregas = query.execute();

            if (entregas.isEmpty()) {
                System.out.println("Nenhuma entrega encontrada.");
            } else {
                System.out.println("📦 Lista de Entregas:");
                for (Entrega e : entregas) {
                    System.out.println("----------------------------------------");
                    System.out.println("ID: " + e.getId());
                    System.out.println("Data: " + e.getData());
                    System.out.println("Localização:");
                    System.out.println("  Latitude: " + e.getLatitude());
                    System.out.println("  Longitude: " + e.getLongitude());
                    System.out.println("Entregador: " + (e.getEntregador() != null ? e.getEntregador().getNome() : "N/A"));
                    System.out.println("Pedidos: " + e.getPedidos());
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar entregas: " + e.getMessage());
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new ListarEntrega();
    }
}
