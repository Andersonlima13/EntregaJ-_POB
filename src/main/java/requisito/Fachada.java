package requisito;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import model.Entrega;
import model.Entregador;
import model.Pedido;
import repositorio.EntregaRepositorio;
import repositorio.EntregadorRepositorio;
import repositorio.PedidoRepositorio;
import util.Util;

public class Fachada {

    private static EntregadorRepositorio entregadorRep = new EntregadorRepositorio();
    private static EntregaRepositorio entregaRep = new EntregaRepositorio();
    private static PedidoRepositorio pedidoRep = new PedidoRepositorio();

    // =========================
    // ENTREGADOR
    // =========================
    public static void criarEntregador(String nome) throws Exception {
        if (nome == null || nome.isEmpty())
            throw new Exception("Nome vazio");

        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();
            entregadorRep.setEntityManager(em);

            Entregador existente = entregadorRep.ler(nome);
            if (existente != null)
                throw new Exception("Entregador já existe: " + nome);

            entregadorRep.criar(new Entregador(nome));

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static List<Entregador> listarEntregadores() {
        EntityManager em = Util.getEntityManager();
        try {
            entregadorRep.setEntityManager(em);
            List<Entregador> lista = entregadorRep.listar();

            // força inicialização
            for (Entregador e : lista) {
                e.getListaDeEntrega().size();
            }

            return lista;
        } finally {
            em.close();
        }
    }
    
    
    

    public static Entregador localizarEntregadorPorNome(String nome) {
        EntityManager em = Util.getEntityManager();
        try {
            entregadorRep.setEntityManager(em);
            return entregadorRep.ler(nome);
        } finally {
            em.close();
        }
    }

    public static void atualizarEntregador(int id, String novoNome) throws Exception {
        if (novoNome == null || novoNome.isEmpty())
            throw new Exception("Nome vazio");

        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();
            entregadorRep.setEntityManager(em);

            Entregador e = entregadorRep.ler(id);
            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            Entregador outro = entregadorRep.ler(novoNome);
            if (outro != null && outro.getId() != id)
                throw new Exception("Outro entregador já usa o nome: " + novoNome);

            e.setNome(novoNome);
            entregadorRep.atualizar(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static void apagarEntregador(int id) throws Exception {
        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();
            entregadorRep.setEntityManager(em);

            Entregador e = entregadorRep.ler(id);
            if (e == null)
                throw new Exception("Entregador inexistente: " + id);

            if (e.getListaDeEntrega() != null && !e.getListaDeEntrega().isEmpty())
                throw new Exception("Entregador possui entregas vinculadas.");

            entregadorRep.apagar(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    // =========================
    // PEDIDO
    // =========================
    public static void criarPedido(Pedido p) throws Exception {
        if (p == null)
            throw new Exception("Pedido nulo");

        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();
            pedidoRep.setEntityManager(em);

            if (p.getId() != 0 && pedidoRep.ler(p.getId()) != null)
                throw new Exception("Pedido já existe id:" + p.getId());

            pedidoRep.criar(p);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public static List<Pedido> listarPedidos() {
        EntityManager em = Util.getEntityManager();
        try {
            pedidoRep.setEntityManager(em);
            return pedidoRep.listar();
        } finally {
            em.close();
        }
    }

    public static Pedido localizarPedidoPorId(int id) {
        EntityManager em = Util.getEntityManager();
        try {
            pedidoRep.setEntityManager(em);
            return pedidoRep.ler(id);
        } finally {
            em.close();
        }
    }

    public static void apagarPedido(int id) throws Exception {
        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();
            pedidoRep.setEntityManager(em);
            entregaRep.setEntityManager(em);

            Pedido p = pedidoRep.ler(id);
            if (p == null)
                throw new Exception("Pedido inexistente: " + id);

            if (p.getEntrega() != null) {
                Entrega ent = p.getEntrega();
                ent.getPedidos().removeIf(x -> x.getId() == id);
                entregaRep.atualizar(ent);
            }

            pedidoRep.apagar(p);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    // =========================
    // ENTREGA
    // =========================
    public static void criarEntrega(Entrega ent) throws Exception {
        if (ent == null)
            throw new Exception("Entrega nula");

        if (ent.getEntregador() == null)
            throw new Exception("Entrega deve ter entregador");

        if (ent.getPedidos().size() > 2)
            throw new Exception("Entrega não pode ter mais de dois pedidos");

        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();

            entregadorRep.setEntityManager(em);
            pedidoRep.setEntityManager(em);
            entregaRep.setEntityManager(em);

            // 🔹 Reanexa o entregador
            Entregador entregador = entregadorRep.ler(ent.getEntregador().getId());
            if (entregador == null)
                throw new Exception("Entregador inexistente");

            ent.setEntregador(entregador);

            // 🔹 Reanexa os pedidos
            List<Pedido> pedidosGerenciados = new ArrayList<>();
            for (Pedido p : ent.getPedidos()) {
                Pedido pedidoGerenciado = pedidoRep.ler(p.getId());
                if (pedidoGerenciado == null)
                    throw new Exception("Pedido inexistente id:" + p.getId());

                pedidosGerenciados.add(pedidoGerenciado);
            }

            // limpa e reconecta corretamente
            ent.getPedidos().clear();
            for (Pedido p : pedidosGerenciados) {
                ent.adicionarPedido(p);
            }

            // 🔹 Agora tudo está MANAGED
            entregaRep.criar(ent);

            entregador.getListaDeEntrega().add(ent);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
    
    
    

    public static List<Entrega> listarEntregas() {
        EntityManager em = Util.getEntityManager();
        try {
            entregaRep.setEntityManager(em);
            List<Entrega> lista = entregaRep.listar();

            // 🔹 força inicialização dos pedidos
            for (Entrega e : lista) {
                e.getPedidos().size();
            }

            return lista;
        } finally {
            em.close();
        }
    }
    
    
    
    public static Entrega localizarEntregaPorId(int id) {
        EntityManager em = Util.getEntityManager();
        try {
            entregaRep.setEntityManager(em);
            return entregaRep.ler(id);
        } finally {
            em.close();
        }
    }

    public static void apagarEntrega(int id) throws Exception {
        EntityManager em = Util.getEntityManager();
        try {
            em.getTransaction().begin();

            entregaRep.setEntityManager(em);
            entregadorRep.setEntityManager(em);

            Entrega e = entregaRep.ler(id);
            if (e == null)
                throw new Exception("Entrega inexistente: " + id);

            Entregador ent = e.getEntregador();
            if (ent != null)
                ent.getListaDeEntrega().removeIf(x -> x.getId() == id);

            entregaRep.apagar(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
