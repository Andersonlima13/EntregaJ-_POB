package model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "entrega20232370027")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 20)
    private String data;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "entregador_id", nullable = false)
    private Entregador entregador;

    // 🔴 List -> 🟢 Set
    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Pedido> pedidos = new HashSet<>();

    public Entrega() {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Entregador getEntregador() {
        return entregador;
    }

    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    // Mantém relação bidirecional
    public void adicionarPedido(Pedido p) {
        pedidos.add(p);
        p.setEntrega(this);
    }

    @Override
    public String toString() {
        return "\nEntrega {" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", entregador=" + (entregador != null ? entregador.getNome() : "N/A") +
                ", totalPedidos=" + pedidos.size() +
                '}';
    }
}
