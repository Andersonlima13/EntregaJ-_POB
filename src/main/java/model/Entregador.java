package model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="entregador20232370027")
public class Entregador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    
    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Entrega> listaDeEntrega = new ArrayList<>();

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
                ", temFoto=" + (foto != null && foto.length > 0) +
                '}';
    }
}