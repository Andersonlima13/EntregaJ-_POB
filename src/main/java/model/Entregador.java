package model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entregador {
    private String id; // agora é String para armazenar UUID
    private String nome;
    private List<Entrega> listaDeEntrega;

    public Entregador(String nome) {
        this.id = UUID.randomUUID().toString().substring(0, 4); // gera um ID curto e único
        this.nome = nome;
        this.listaDeEntrega = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    // não precisamos mais de setId, mas pode manter se quiser
    public void setId(String id) {
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
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", totalEntregas=" + listaDeEntrega.size() +
                '}';
    }
}
