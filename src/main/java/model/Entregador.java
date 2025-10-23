package model;

import java.util.ArrayList;
import java.util.List;

public class Entregador {
    private int id; // agora é int gerado automaticamente
    private String nome;
    private List<Entrega> listaDeEntrega;

    public Entregador(String nome) {
        this.nome = nome;
        this.listaDeEntrega = new ArrayList<>();
        // id será gerado automaticamente pelo Util.java
    }

    public int getId() {
        return id;
    }

    // Mantido caso necessário
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Entrega> getListaDeEntrega() {
        return listaDeEntrega;
    }

    public void setListaDeEntrega(List<Entrega> listaDeEntrega) {
        this.listaDeEntrega = listaDeEntrega;
    }

    @Override
    public String toString() {
        return "\nEntregador {" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", totalEntregas=" + listaDeEntrega.size() +
                '}';
    }
}
