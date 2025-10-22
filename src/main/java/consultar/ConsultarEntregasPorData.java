package consultar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entrega;

import java.util.List;

public class ConsultarEntregasPorData {
    private ObjectContainer manager;

    public ConsultarEntregasPorData(String data) {
        manager = Util.conectarBanco();

        try {
            Query query = manager.query();
            query.constrain(Entrega.class);
            query.descend("data").constrain(data);

            List<Entrega> entregas = query.execute();

            if (entregas.isEmpty()) {
                System.out.println("❌ Nenhuma entrega encontrada na data: " + data);
            } else {
                System.out.println("📦 Entregas na data " + data + ":");
                for (Entrega e : entregas) {
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar entregas por data: " + e.getMessage());
        } finally {
            Util.desconectar();
        }
    }

    public static void main(String[] args) {
        new ConsultarEntregasPorData("17/05/2025");
    }
}
