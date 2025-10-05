package model;


public class Pedido {
    public Integer id;
    public String data;
    public Double valor;
    public String descricao;

    public Pedido(Integer id){
        this.id = id;
    }


    public String getData() {
        return data;
    }

    public String setData(String data){
        this.data = data;
        return data;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValor() {
        return valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}



