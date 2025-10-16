package model;

import java.util.UUID;

public class Pedido {
    private String id; //id gerado via uuid
    private String data;
    private Double valor;
    private String descricao;
    private Entrega entrega; // relação: cada pedido pertence a uma entrega

    // Construtor padrão (gera UUID automaticamente)
    public Pedido() {
        this.id = UUID.randomUUID().toString().substring(0, 4);
    }

    // Getter e Setter
    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    // método para printar o pedido
    @Override
    public String toString() {
        return "\nPedido {" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
