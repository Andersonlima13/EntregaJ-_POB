package requisito;

import java.util.List;

import model.Entrega;
import model.Entregador;
import model.Pedido;
import repositorio.EntregaRepositorio;
import repositorio.EntregadorRepositorio;
import repositorio.PedidoRepositorio;

public class Fachada {

    private static EntregadorRepositorio entregadorRep = new EntregadorRepositorio();
    private static EntregaRepositorio entregaRep = new EntregaRepositorio();
    private static PedidoRepositorio pedidoRep = new PedidoRepositorio();

    // ---------------------------------------------------------
    // ENTREGADOR
    // ---------------------------------------------------------
    public static void criarEntregador(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            throw new Exception("Nome vazio");

        Entregador existente = entregadorRep.ler(nome);
        if (existente != null)
            throw new Exception("Entregador já existe: " + nome);

        Entregador e = new Entregador(nome);

        entregadorRep.conectar();
        entregadorRep.criar(e);
        entregadorRep.commit();
        entregadorRep.desconectar();
    }

    public static List<Entregador> listarEntregadores() {
        entregadorRep.conectar();
        List<Entregador> lista = entregadorRep.listar();
        entregadorRep.desconectar();
        return lista;
    }

    public static Entregador localizarEntregadorPorNome(String nome) {
        return entregadorRep.ler(nome);
    }

    // ---------------------------------------------------------
    // PEDIDO
    // ---------------------------------------------------------
    public static void criarPedido(Pedido p) throws Exception {
        if (p == null)
            throw new Exception("Pedido nulo");

        if (p.getId() != 0) {
            Pedido existe = pedidoRep.ler(p.getId());
            if (existe != null)
                throw new Exception("Pedido já existe id:" + p.getId());
        }

        pedidoRep.conectar();
        pedidoRep.criar(p);
        pedidoRep.commit();
        pedidoRep.desconectar();
    }

    public static List<Pedido> listarPedidos() {
        pedidoRep.conectar();
        List<Pedido> lista = pedidoRep.listar();
        pedidoRep.desconectar();
        return lista;
    }

    public static Pedido localizarPedidoPorId(int id) {
        return pedidoRep.ler(id);
    }

    // ---------------------------------------------------------
    // ENTREGA
    // ---------------------------------------------------------
    public static void criarEntrega(Entrega ent) throws Exception {
        if (ent == null)
            throw new Exception("Entrega nula");

        if (ent.getEntregador() == null)
            throw new Exception("Uma entrega deve ter um entregador");

        if (ent.getPedidos().size() > 2)
            throw new Exception("Uma entrega não pode ter mais de dois pedidos");

        // pedidos duplicados
        List<Pedido> pedidos = ent.getPedidos();
        for (int i = 0; i < pedidos.size(); i++) {
            for (int j = i + 1; j < pedidos.size(); j++) {
                if (pedidos.get(i).getId() == pedidos.get(j).getId())
                    throw new Exception("Pedido duplicado id:" + pedidos.get(i).getId());
            }
        }

        // entregador deve existir
        Entregador entregador = entregadorRep.ler(ent.getEntregador().getNome());
        if (entregador == null)
            throw new Exception("Entregador inexistente: " + ent.getEntregador().getNome());

        // pedidos devem existir
        for (Pedido p : pedidos) {
            Pedido existe = pedidoRep.ler(p.getId());
            if (existe == null)
                throw new Exception("Pedido inexistente id:" + p.getId());
        }

        // criar entrega
        entregaRep.conectar();
        entregaRep.criar(ent);
        entregaRep.commit();
        entregaRep.desconectar();

        // vincular entrega ao entregador
        entregadorRep.conectar();
        entregador.getListaDeEntrega().add(ent);
        entregadorRep.atualizar(entregador);
        entregadorRep.commit();
        entregadorRep.desconectar();
    }

    public static List<Entrega> listarEntregas() {
        entregaRep.conectar();
        List<Entrega> lista = entregaRep.listar();
        entregaRep.desconectar();
        return lista;
    }

    public static Entrega localizarEntregaPorId(int id) {
        return entregaRep.ler(id);
    }

    public static void apagarEntrega(int id) throws Exception {
        Entrega e = entregaRep.ler(id);
        if (e == null)
            throw new Exception("Entrega inexistente: " + id);

        // remover dos entregadores
        Entregador ent = e.getEntregador();
        if (ent != null && ent.getListaDeEntrega() != null) {
            ent.getListaDeEntrega().removeIf(x -> x.getId() == e.getId());

            entregadorRep.conectar();
            entregadorRep.atualizar(ent);
            entregadorRep.commit();
            entregadorRep.desconectar();
        }

        // apagar entrega
        entregaRep.conectar();
        entregaRep.apagar(e);
        entregaRep.commit();
        entregaRep.desconectar();
    }
}
