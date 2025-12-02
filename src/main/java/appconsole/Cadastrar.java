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
            Fachada.criarEntregador("Ana Oliveira");
            Fachada.criarEntregador("Lucas Pereira");
            Fachada.criarEntregador("Carla Souza");
            Fachada.criarEntregador("Bruno Lima");

            System.out.println("\nCadastrando Pedidos via Fachada...");
            Pedido p1 = new Pedido();
            p1.setData("15/01/2025");
            p1.setValor(45.90);
            p1.setDescricao("Pizza calabresa + refrigerante");
            Fachada.criarPedido(p1);

            Pedido p2 = new Pedido();
            p2.setData("15/01/2025");
            p2.setValor(32.50);
            p2.setDescricao("Hambúrguer artesanal");
            Fachada.criarPedido(p2);

            Pedido p3 = new Pedido();
            p3.setData("16/01/2025");
            p3.setValor(28.00);
            p3.setDescricao("Sushi combo");
            Fachada.criarPedido(p3);

            Pedido p4 = new Pedido();
            p4.setData("16/01/2025");
            p4.setValor(19.90);
            p4.setDescricao("Açaí 500ml");
            Fachada.criarPedido(p4);

            Pedido p5 = new Pedido();
            p5.setData("17/01/2025");
            p5.setValor(55.00);
            p5.setDescricao("Marmita fitness");
            Fachada.criarPedido(p5);

            Pedido p6 = new Pedido();
            p6.setData("17/01/2025");
            p6.setValor(12.50);
            p6.setDescricao("Coxinha + suco");
            Fachada.criarPedido(p6);

            Pedido p7 = new Pedido();
            p7.setData("18/01/2025");
            p7.setValor(80.00);
            p7.setDescricao("Churrasco para 2");
            Fachada.criarPedido(p7);

            Pedido p8 = new Pedido();
            p8.setData("19/01/2025");
            p8.setValor(15.00);
            p8.setDescricao("Sanduíche natural");
            Fachada.criarPedido(p8);

            Pedido p9 = new Pedido();
            p9.setData("19/01/2025");
            p9.setValor(9.90);
            p9.setDescricao("Água + sobremesa");
            Fachada.criarPedido(p9);

            Pedido p10 = new Pedido();
            p10.setData("19/01/2025");
            p10.setValor(120.00);
            p10.setDescricao("Jantar completo para 4");
            Fachada.criarPedido(p10);

            Pedido p11 = new Pedido();
            p11.setData("20/01/2025");
            p11.setValor(7.50);
            p11.setDescricao("Café da manhã delivery");
            Fachada.criarPedido(p11);

            System.out.println("\nCadastrando Entregas via Fachada...");

            Entregador ent1 = Fachada.localizarEntregadorPorNome("João Silva");
            Entregador ent2 = Fachada.localizarEntregadorPorNome("Maria Santos");
            Entregador ent3 = Fachada.localizarEntregadorPorNome("Pedro Costa");
            Entregador ent4 = Fachada.localizarEntregadorPorNome("Ana Oliveira");
            Entregador ent5 = Fachada.localizarEntregadorPorNome("Lucas Pereira");

            Entrega e1 = new Entrega();
            e1.setData("15/01/2025");
            e1.setLatitude(-7.1186);
            e1.setLongitude(-34.8811);
            e1.setEntregador(ent1);
            e1.adicionarPedido(Fachada.localizarPedidoPorId(1));
            e1.adicionarPedido(Fachada.localizarPedidoPorId(2));
            Fachada.criarEntrega(e1);

            Entrega e2 = new Entrega();
            e2.setData("16/01/2025");
            e2.setLatitude(-7.1200);
            e2.setLongitude(-34.8850);
            e2.setEntregador(ent2);
            e2.adicionarPedido(Fachada.localizarPedidoPorId(3));
            e2.adicionarPedido(Fachada.localizarPedidoPorId(4));
            Fachada.criarEntrega(e2);

            Entrega e3 = new Entrega();
            e3.setData("17/01/2025");
            e3.setLatitude(-7.1150);
            e3.setLongitude(-34.8790);
            e3.setEntregador(ent1);
            e3.adicionarPedido(Fachada.localizarPedidoPorId(5));
            Fachada.criarEntrega(e3);

            Entrega e4 = new Entrega();
            e4.setData("18/01/2025");
            e4.setLatitude(-7.1190);
            e4.setLongitude(-34.8820);
            e4.setEntregador(ent3);
            e4.adicionarPedido(Fachada.localizarPedidoPorId(6));
            e4.adicionarPedido(Fachada.localizarPedidoPorId(7));
            Fachada.criarEntrega(e4);

            Entrega e5 = new Entrega();
            e5.setData("19/01/2025");
            e5.setLatitude(-7.1175);
            e5.setLongitude(-34.8805);
            e5.setEntregador(ent4);
            e5.adicionarPedido(Fachada.localizarPedidoPorId(8));
            Fachada.criarEntrega(e5);

            Entrega e6 = new Entrega();
            e6.setData("20/01/2025");
            e6.setLatitude(-7.1160);
            e6.setLongitude(-34.8780);
            e6.setEntregador(ent5);
            e6.adicionarPedido(Fachada.localizarPedidoPorId(9));
            e6.adicionarPedido(Fachada.localizarPedidoPorId(10));
            Fachada.criarEntrega(e6);

            Entrega e7 = new Entrega();
            e7.setData("21/01/2025");
            e7.setLatitude(-7.1155);
            e7.setLongitude(-34.8795);
            e7.setEntregador(ent2);
            e7.adicionarPedido(Fachada.localizarPedidoPorId(11));
            Fachada.criarEntrega(e7);

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