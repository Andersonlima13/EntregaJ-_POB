package aplicacao;

import Modelo.Pedido;
import Modelo.Entrega;
import Modelo.Entregador;

public class Alterar {
    public static void removerPedidoDeEntrega(Entrega entrega, Pedido pedido) {
        if (entrega != null && pedido != null) {
            entrega.getListaDePedido().remove(pedido);
            if (pedido.getEntrega() == entrega) {
                pedido.setEntrega(null);
            }
        }
    }

    public static void main(String[] args) {
        
        Entregador entregador = new Entregador("João");
        Entrega entrega = new Entrega("2025-10-10", "Centro", entregador);
        Pedido pedido1 = new Pedido("2025-10-10", 100.0, "Pedido 1", entrega);
        Pedido pedido2 = new Pedido("2025-10-10", 200.0, "Pedido 2", entrega);
 
        entrega.getListaDePedido().add(pedido1);
        entrega.getListaDePedido().add(pedido2);
       
        System.out.println("Antes de remover: " + entrega.getListaDePedido().size() + " pedidos");
        
        removerPedidoDeEntrega(entrega, pedido1);

        System.out.println("Depois de remover: " + entrega.getListaDePedido().size() + " pedidos");
        System.out.println("Entrega do pedido1: " + pedido1.getEntrega());
    }
}