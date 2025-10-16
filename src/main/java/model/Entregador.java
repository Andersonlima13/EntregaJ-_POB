package model;

import java.util.ArrayList;
import java.util.List;

public class Entregador {
    private Integer id;
    private String nome;
    private List<Entrega> entregas; // relação: um entregador pode ter várias entregas

    public Entregador(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
        this.entregas = new ArrayList<>();
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }

    public List<Entrega> getEntregas() { return entregas; }

    public void adicionarEntrega(Entrega e) {
        entregas.add(e);
        e.setEntregador(this); // mantém a relação bidirecional
    }


}
