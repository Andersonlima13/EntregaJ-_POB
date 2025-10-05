package model;

import java.util.Date;

public class pedido {
    public Integer id;
    public Date data;
    public Double valor;
    public String descricao;

    public pedido(Integer id , Date data, Double valor, String descricao){
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Date getData() {
        return data;
    }
    public Date setData(Date data){
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



