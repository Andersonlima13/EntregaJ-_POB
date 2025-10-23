package model;

public class Pedido {
    private int id; // agora é int gerado automaticamente
    private String data;
    private Double valor;
    private String descricao;
    private Entrega entrega; // cada pedido pertence a uma entrega

    // Construtor padrão
    public Pedido() {
        // id será gerado automaticamente pelo Util.java
    }

    // Getters e Setters
    public int getId() {
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

    @Override
    public String toString() {
        return "\nPedido {" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
