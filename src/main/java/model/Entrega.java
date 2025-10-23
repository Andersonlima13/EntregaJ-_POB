package model;

import java.util.ArrayList;
import java.util.List;

public class Entrega {
    private int id; // agora é int gerado automaticamente
    private String data;
    private double latitude;
    private double longitude;
    private Entregador entregador; // uma entrega pertence a um entregador
    private List<Pedido> pedidos;  // uma entrega tem vários pedidos

    public Entrega() {
        this.pedidos = new ArrayList<>();
        // id será gerado automaticamente pelo Util.java
    }

    // Getters e Setters
    public int getId() { return id; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Entregador getEntregador() { return entregador; }

    public void setEntregador(Entregador entregador) {
        if (entregador == null) {
            throw new IllegalArgumentException("Uma entrega deve ter um entregador.");
        }
        this.entregador = entregador;
    }
    public List<Pedido> getPedidos() { return pedidos; }

    public void adicionarPedido(Pedido p) {
        pedidos.add(p);
        p.setEntrega(this); // mantém a relação bidirecional
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
