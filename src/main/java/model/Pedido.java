package model;

import jakarta.persistence.*;


@Entity
@Table(name="pedido20232370027")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(length = 20)
    private String data;
    
    @Column
    private Double valor;
    
    @Column(length = 200)
    private String descricao;
    
    @ManyToOne
    @JoinColumn(name = "entrega_id")
    private Entrega entrega;

    // Construtores
    public Pedido() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                ", entrega=" + (entrega != null ? "#" + entrega.getId() : "sem entrega") +
                '}';
    }
}