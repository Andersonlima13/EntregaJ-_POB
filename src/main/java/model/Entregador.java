package model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "entregador20232370027")
public class Entregador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    // 🔴 List -> 🟢 Set (evita MultipleBagFetchException)
    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entrega> listaDeEntrega = new HashSet<>();

    public Entregador() {
    }

    public Entregador(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Set<Entrega> getListaDeEntrega() {
        return listaDeEntrega;
    }

    public void setListaDeEntrega(Set<Entrega> listaDeEntrega) {
        this.listaDeEntrega = listaDeEntrega;
    }

    // Método auxiliar (boa prática)
    public void adicionarEntrega(Entrega e) {
        listaDeEntrega.add(e);
        e.setEntregador(this);
    }

    @Override
    public String toString() {
        return "\nEntregador {" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", totalEntregas=" + listaDeEntrega.size() +
                ", temFoto=" + (foto != null && foto.length > 0) +
                '}';
    }
}
