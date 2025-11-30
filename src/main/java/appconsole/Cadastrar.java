package appconsole;

import requisito.Fachada;
import model.Entrega;
import model.Entregador;
import model.Pedido;

public class Cadastrar {

    public Cadastrar() {
        try {
            System.out.println("=== CADASTRANDO OBJETOS (através da Fachada) ===\n");

            System.out.println("Cadastrando Entregadores via Fachada...");
            Fachada.criarEntregador("João Silva");
            Fachada.criarEntregador("Maria Santos");
            Fachada.criarEntregador("Pedro Costa");

            System.out.println("\nCadastrando Pedidos via Fachada...");
            Pedido p1 = new Pedido();
            p1.setData("2025-01-15");
            p1.setValor(45.90);
            p1.setDescricao("Pizza calabresa + refrigerante");
            Fachada.criarPedido(p1);

            Pedido p2 = new Pedido();
            p2.setData("2025-01-15");
            p2.setValor(32.50);
            p2.setDescricao("Hambúrguer artesanal");
            Fachada.criarPedido(p2);

            Pedido p3 = new Pedido();
            p3.setData("2025-01-16");
            p3.setValor(28.00);
            p3.setDescricao("Sushi combo");
            Fachada.criarPedido(p3);

            Pedido p4 = new Pedido();
            p4.setData("2025-01-16");
            p4.setValor(19.90);
            p4.setDescricao("Açaí 500ml");
            Fachada.criarPedido(p4);

            Pedido p5 = new Pedido();
            p5.setData("2025-01-17");
            p5.setValor(55.00);
            p5.setDescricao("Marmita fitness");
            Fachada.criarPedido(p5);

            System.out.println("\nCadastrando Entregas via Fachada...");

            Entregador ent1 = Fachada.localizarEntregadorPorNome("João Silva");
            Entregador ent2 = Fachada.localizarEntregadorPorNome("Maria Santos");

            Entrega e1 = new Entrega();
            e1.setData("2025-01-15");
            e1.setLatitude(-7.1186);
            e1.setLongitude(-34.8811);
            e1.setEntregador(ent1);
            e1.adicionarPedido(Fachada.localizarPedidoPorId(1));
            e1.adicionarPedido(Fachada.localizarPedidoPorId(2));
            Fachada.criarEntrega(e1);

            Entrega e2 = new Entrega();
            e2.setData("2025-01-16");
            e2.setLatitude(-7.1200);
            e2.setLongitude(-34.8850);
            e2.setEntregador(ent2);
            e2.adicionarPedido(Fachada.localizarPedidoPorId(3));
            e2.adicionarPedido(Fachada.localizarPedidoPorId(4));
            Fachada.criarEntrega(e2);

            Entrega e3 = new Entrega();
            e3.setData("2025-01-17");
            e3.setLatitude(-7.1150);
            e3.setLongitude(-34.8790);
            e3.setEntregador(ent1);
            e3.adicionarPedido(Fachada.localizarPedidoPorId(5));
            Fachada.criarEntrega(e3);

            System.out.println("\n=== CADASTRO CONCLUÍDO (via Fachada) ===");

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar via Fachada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cadastrar();
    }
}