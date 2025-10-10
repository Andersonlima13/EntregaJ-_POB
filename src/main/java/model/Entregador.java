package Modelo;
import java.util.ArrayList;
import java.util.List;

public class Entregador {
    private Integer id;
    private String nome;
    private List<Entrega> listaDeEntrega;

    public Entregador(String nome) {
        this.id = 0;
        this.nome = nome;
        this.listaDeEntrega = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setListaDeEntrega(List<Entrega> listaDeEntrega) {
        this.listaDeEntrega = listaDeEntrega;
    }
}