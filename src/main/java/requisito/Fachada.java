package requisito;

import java.util.ArrayList;
import java.util.List;

import model.Entrega;
import model.Entregador;
import model.Pedido;
import repositorio.EntregaRepositorio;
import repositorio.EntregadorRepositorio;
import repositorio.PedidoRepositorio;

public class Fachada {

    private Fachada() {}

    private static EntregadorRepositorio entregadorRep = new EntregadorRepositorio();
    private static EntregaRepositorio entregaRep = new EntregaRepositorio();
    private static PedidoRepositorio pedidoRep = new PedidoRepositorio();

    // =========================
    // ENTREGADOR
    // =========================
    public static void criarEntregador(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            throw new Exception("Nome vazio");

        try {
            entregadorRep.conectar();
            entregadorRep.begin();

            Entregador existente = entregadorRep.ler(nome);
            if (existente != null)
                throw new Exception("Entregador já existe: " + nome);

            entregadorRep.criar(new Entregador(nome));
            entregadorRep.commit();

        } catch (Exception e) {
            entregadorRep.rollback();
            throw e;
        } finally {
            entregadorRep.desconectar();
        }
    }

    public static List<Entregador> listarEntregadores() {
        try {
            entregadorRep.conectar();
            List<Entregador> lista = entregadorRep.listar();

            for (Entregador e : lista) {
                // força lazy
                e.getListaDeEntrega().size();

                // força carga da foto
                if (e.getFoto() != null) {
                    int len = e.getFoto().length;
                    if (len < 0) {}
                }
            }
            return lista;

        } finally {
            entregadorRep.desconectar();
        }
    }

    public static Entregador localizarEntregadorPorNome(String nome) throws Exception {
        try {
            entregadorRep.conectar();
            Entregador e = entregadorRep.ler(nome);

            if (e == null)
                throw new Exception("Entregador inexistente: " + nome);

            // lazy + foto
            e.getListaDeEntrega().size();
            if (e.getFoto() != null) {
                int len = e.getFoto().length;
                if (len < 0) {}
            }

            return e;

        } finally {
            entregadorRep.desconectar();
        }
    }

    public static Entregador localizarEntregadorPorId(int id) throws Exception {
        try {
            entregadorRep.conectar();
            Entregador e = entregadorRep.ler(id);

            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            e.getListaDeEntrega().size();

            if (e.getFoto() != null) {
                int len = e.getFoto().length;
                if (len < 0) {}
            }

            return e;

        } finally {
            entregadorRep.desconectar();
        }
    }

    public static void atualizarFotoEntregador(int id, byte[] foto) throws Exception {
        if (foto == null)
            throw new Exception("Foto inválida");

        try {
            entregadorRep.conectar();
            entregadorRep.begin();

            Entregador e = entregadorRep.ler(id);
            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            e.setFoto(foto);
            entregadorRep.atualizar(e);

            entregadorRep.commit();

        } catch (Exception ex) {
            entregadorRep.rollback();
            throw ex;
        } finally {
            entregadorRep.desconectar();
        }
    }

    public static void atualizarEntregador(int id, String novoNome) throws Exception {
        if (novoNome == null || novoNome.isEmpty())
            throw new Exception("Nome vazio");

        try {
            entregadorRep.conectar();
            entregadorRep.begin();

            Entregador e = entregadorRep.ler(id);
            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            Entregador outro = entregadorRep.ler(novoNome);
            if (outro != null && outro.getId() != id)
                throw new Exception("Outro entregador já usa o nome: " + novoNome);

            e.setNome(novoNome);
            entregadorRep.atualizar(e);
            entregadorRep.commit();

        } catch (Exception ex) {
            entregadorRep.rollback();
            throw ex;
        } finally {
            entregadorRep.desconectar();
        }
    }

    public static void apagarEntregador(int id) throws Exception {
        try {
            entregadorRep.conectar();
            entregadorRep.begin();

            Entregador e = entregadorRep.ler(id);
            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            if (!e.getListaDeEntrega().isEmpty())
                throw new Exception("Entregador possui entregas vinculadas");

            entregadorRep.apagar(e);
            entregadorRep.commit();

        } catch (Exception ex) {
            entregadorRep.rollback();
            throw ex;
        } finally {
            entregadorRep.desconectar();
        }
    }

    // =========================
    // PEDIDO
    // =========================
    public static void criarPedido(Pedido p) throws Exception {
        if (p == null)
            throw new Exception("Pedido nulo");

        try {
            pedidoRep.conectar();
            pedidoRep.begin();

            pedidoRep.criar(p);
            pedidoRep.commit();

        } catch (Exception ex) {
            pedidoRep.rollback();
            throw ex;
        } finally {
            pedidoRep.desconectar();
        }
    }

    public static List<Pedido> listarPedidos() {
        try {
            pedidoRep.conectar();
            return pedidoRep.listar();
        } finally {
            pedidoRep.desconectar();
        }
    }

    public static Pedido localizarPedidoPorId(int id) throws Exception {
        try {
            pedidoRep.conectar();
            Pedido p = pedidoRep.ler(id);

            if (p == null)
                throw new Exception("Pedido inexistente: " + id);

            return p;

        } finally {
            pedidoRep.desconectar();
        }
    }

    public static void apagarPedido(int id) throws Exception {
        try {
            pedidoRep.conectar();
            entregaRep.conectar();

            pedidoRep.begin();

            Pedido p = pedidoRep.ler(id);
            if (p == null)
                throw new Exception("Pedido inexistente: " + id);

            if (p.getEntrega() != null) {
                Entrega ent = p.getEntrega();
                ent.getPedidos().removeIf(x -> x.getId() == id);
                entregaRep.atualizar(ent);
            }

            pedidoRep.apagar(p);
            pedidoRep.commit();

        } catch (Exception ex) {
            pedidoRep.rollback();
            throw ex;
        } finally {
            pedidoRep.desconectar();
            entregaRep.desconectar();
        }
    }


    // =========================
    // ENTREGA
    // =========================
    
    
    public static List<Entrega> listarEntregas() {
        try {
            entregaRep.conectar();
            entregadorRep.conectar();

            List<Entrega> lista = entregaRep.listar();

            for (Entrega e : lista) {
                // força lazy dos pedidos
                e.getPedidos().size();

                // força lazy do entregador
                if (e.getEntregador() != null) {
                    e.getEntregador().getNome();

                    // força carga da foto do entregador (se existir)
                    if (e.getEntregador().getFoto() != null) {
                        int len = e.getEntregador().getFoto().length;
                        if (len < 0) {}
                    }
                }
            }

            return lista;

        } finally {
            entregaRep.desconectar();
            entregadorRep.desconectar();
        }
    }
    
    
    public static void apagarEntrega(int id) throws Exception {
        try {
            entregaRep.conectar();
            pedidoRep.conectar();
            entregadorRep.conectar();

            entregaRep.begin();

            Entrega ent = entregaRep.ler(id);
            if (ent == null)
                throw new Exception("Entrega inexistente: " + id);

            // desvincula pedidos
            for (Pedido p : ent.getPedidos()) {
                p.setEntrega(null);
                pedidoRep.atualizar(p);
            }
            ent.getPedidos().clear();

            // desvincula do entregador
            Entregador e = ent.getEntregador();
            if (e != null) {
                e.getListaDeEntrega().removeIf(x -> x.getId() == id);
                entregadorRep.atualizar(e);
            }

            entregaRep.apagar(ent);
            entregaRep.commit();

        } catch (Exception ex) {
            entregaRep.rollback();
            throw ex;
        } finally {
            entregaRep.desconectar();
            pedidoRep.desconectar();
            entregadorRep.desconectar();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void criarEntrega(Entrega ent) throws Exception {
        if (ent == null)
            throw new Exception("Entrega nula");

        if (ent.getEntregador() == null)
            throw new Exception("Entrega deve ter entregador");

        if (ent.getPedidos().size() > 2)
            throw new Exception("Entrega não pode ter mais de dois pedidos");

        try {
            entregadorRep.conectar();
            pedidoRep.conectar();
            entregaRep.conectar();

            entregaRep.begin();

            Entregador entregador = entregadorRep.ler(ent.getEntregador().getId());
            if (entregador == null)
                throw new Exception("Entregador inexistente");

            ent.setEntregador(entregador);

            List<Pedido> pedidosGerenciados = new ArrayList<>();
            for (Pedido p : ent.getPedidos()) {
                Pedido pg = pedidoRep.ler(p.getId());
                if (pg == null)
                    throw new Exception("Pedido inexistente id:" + p.getId());
                pedidosGerenciados.add(pg);
            }

            ent.getPedidos().clear();
            for (Pedido p : pedidosGerenciados) {
                ent.adicionarPedido(p);
            }

            entregaRep.criar(ent);
            entregador.getListaDeEntrega().add(ent);

            entregaRep.commit();

        } catch (Exception ex) {
            entregaRep.rollback();
            throw ex;
        } finally {
            entregadorRep.desconectar();
            pedidoRep.desconectar();
            entregaRep.desconectar();
        }
    }
}
