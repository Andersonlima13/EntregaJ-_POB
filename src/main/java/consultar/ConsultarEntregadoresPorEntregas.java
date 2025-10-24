package consultar;

import banco.Util;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import model.Entregador;

import java.util.List;

public class ConsultarEntregadoresPorEntregas {
    private ObjectContainer manager;

    public ConsultarEntregadoresPorEntregas() {
        manager = Util.conectarBanco();

        try {
            Query query = manager.query();
            query.constrain(Entregador.class);
            List<Entregador> entregadores = query.execute();

            if (entregadores.isEmpty()) {
                System.out.println("❌ Nenhum entregador cadastrado no banco.");
                return;
            }

            System.out.println("🚚 Quantidade de entregas por entregador:");

            for (Entregador e : entregadores) {

                // Garante que a lista nunca seja nula
                if (e.getListaDeEntrega() == null)
                    e.setListaDeEntrega(new java.util.ArrayList<>());

                int qtd = e.getListaDeEntrega().size();

                System.out.println("ID: " + e.getId() +
                        " | Nome: " + e.getNome() +
                        " | Nº de Entregas: " + qtd);
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar entregadores: " + e.getMessage());
        } finally {
            Util.desconectar();
            System.out.println("🔒 Conexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new ConsultarEntregadoresPorEntregas();
    }
}
