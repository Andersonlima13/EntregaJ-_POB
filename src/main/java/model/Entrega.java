package model;
import model.Entregador;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entrega {
    private String id; //id gerado via uuid
    private String data;
    private String localizacao;
    private Entregador entregador; // relação: uma entrega pertence a um entregador
    private List<Pedido> pedidos;  // relação: uma entrega tem vários pedidos

    public Entrega(Integer id) {
        this.id = UUID.randomUUID().toString().substring(0, 4);
        this.pedidos = new ArrayList<>();
    }

    public String getId() { return id; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }


    public Entregador getEntregador() { return entregador; }


    public void setEntregador(Entregador entregador) { this.entregador = entregador; }



    public List<Pedido> getPedidos() { return pedidos; }


    public void adicionarPedido(Pedido p) {
        pedidos.add(p);
        p.setEntrega(this); // mantém a relação bidirecional
    }


}
