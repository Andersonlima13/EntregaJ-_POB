package cadastrar;
import banco.Util;
import model.Pedido;
import com.db4o.ObjectContainer;

public class CadastrarPedido {
    private ObjectContainer manager;

    public CadastrarPedido(){
        manager = Util.conectarBanco();

        System.out.println("Cadastrando pedido");
        Pedido pedido;

        pedido = new Pedido(1);
        pedido.setData("18/05/2025");
        pedido.setValor(20.00);
        pedido.setDescricao("Pedido realizado");

        manager.store(pedido);
        manager.commit();

        System.out.println("Pedido cadastrado");
        Util.desconectar();
        System.out.println("End");
    }

    public static void main(String[] args) {new CadastrarPedido();}

}
