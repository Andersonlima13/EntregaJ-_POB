package Modelo;
import java.util.ArrayList;
import java.util.List;

public class Entrega {
    private Integer id;
    private String data;
    private String localizacao;
    private Entregador entregador;
    private List<Pedido> listaDePedido;

    public Entrega(String data, String localizacao, Entregador entregador) {
        this.id = 0;
        this.data = data;
        this.localizacao = localizacao;
        this.entregador = entregador;
        this.listaDePedido = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Entregador getEntregador() {
        return entregador;
    }

    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    public List<Pedido> getListaDePedido() {
        return listaDePedido;
    }

    public void setListaDePedido(List<Pedido> listaDePedido) {
        this.listaDePedido = listaDePedido;
    }
}