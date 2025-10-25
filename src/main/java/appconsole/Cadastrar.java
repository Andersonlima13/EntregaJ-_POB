package appconsole;

import com.db4o.ObjectContainer;
import model.Entrega;
import model.Entregador;
import model.Pedido;

public class Cadastrar {
    private ObjectContainer manager;

    public Cadastrar() {
        manager = Util.conectarBanco();

        try {
            System.out.println("=== CADASTRANDO OBJETOS NO BANCO ===\n");

            
            System.out.println("Cadastrando Entregadores...");
            Entregador ent1 = new Entregador("João Silva");
            Entregador ent2 = new Entregador("Maria Santos");
            Entregador ent3 = new Entregador("Pedro Costa");

            manager.store(ent1);
            manager.store(ent2);
            manager.store(ent3);
            manager.commit();
            System.out.println("Entregadores cadastrados: " + ent1.getNome() + ", " + ent2.getNome() + ", " + ent3.getNome());

            System.out.println("\nCadastrando Pedidos...");
            Pedido p1 = new Pedido();
            p1.setData("2025-01-15");
            p1.setValor(45.90);
            p1.setDescricao("Pizza calabresa + refrigerante");

            Pedido p2 = new Pedido();
            p2.setData("2025-01-15");
            p2.setValor(32.50);
            p2.setDescricao("Hambúrguer artesanal");

            Pedido p3 = new Pedido();
            p3.setData("2025-01-16");
            p3.setValor(28.00);
            p3.setDescricao("Sushi combo");

            Pedido p4 = new Pedido();
            p4.setData("2025-01-16");
            p4.setValor(19.90);
            p4.setDescricao("Açaí 500ml");

            Pedido p5 = new Pedido();
            p5.setData("2025-01-17");
            p5.setValor(55.00);
            p5.setDescricao("Marmita fitness");

            manager.store(p1);
            manager.store(p2);
            manager.store(p3);
            manager.store(p4);
            manager.store(p5);
            manager.commit();
            System.out.println("pedidos cadastrados com sucesso.");

            System.out.println("\nCadastrando Entregas...");

            // Entrega 1: João Silva entrega 2 pedidos
            Entrega e1 = new Entrega();
            e1.setData("2025-01-15");
            e1.setLatitude(-7.1186);
            e1.setLongitude(-34.8811);
            e1.setEntregador(ent1);
            e1.adicionarPedido(p1);
            e1.adicionarPedido(p2);
            ent1.getListaDeEntrega().add(e1);

            // Entrega 2: Maria Santos entrega 2 pedidos
            Entrega e2 = new Entrega();
            e2.setData("2025-01-16");
            e2.setLatitude(-7.1200);
            e2.setLongitude(-34.8850);
            e2.setEntregador(ent2);
            e2.adicionarPedido(p3);
            e2.adicionarPedido(p4);
            ent2.getListaDeEntrega().add(e2);

            // Entrega 3: João Silva entrega mais 1 pedido 
            Entrega e3 = new Entrega();
            e3.setData("2025-01-17");
            e3.setLatitude(-7.1150);
            e3.setLongitude(-34.8790);
            e3.setEntregador(ent1);
            e3.adicionarPedido(p5);
            ent1.getListaDeEntrega().add(e3);

            manager.store(e1);
            manager.store(e2);
            manager.store(e3);
            manager.store(ent1);
            manager.store(ent2);
            manager.store(p1);
            manager.store(p2);
            manager.store(p3);
            manager.store(p4);
            manager.store(p5);
            manager.commit();

            System.out.println("\n=== CADASTRO CONCLUÍDO ===");
            System.out.println("Total: 3 entregadores, 5 pedidos, 3 entregas");

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Util.desconectar();
            System.out.println("\nConexão encerrada.");
        }
    }

    public static void main(String[] args) {
        new Cadastrar();
    }
}