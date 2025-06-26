package com.pldprojects.myinboxfsg.Models;

import android.content.ClipData;

public class CaixaComItens {
    private Caixas caixa;
    private Itens item;

    // Construtor
    public CaixaComItens(Caixas caixa, Itens item) {
        this.caixa = caixa;
        this.item = item;
    }
    public void setCaixa(Caixas caixa,Itens itens){
        this.caixa = caixa;
        this.item = itens;
    }
    public Caixas getCaixa(){
        return this.caixa;
    }

    public void setItens(Itens itens){
        this.item = itens;
    }
    public Itens getItens(){
        return this.item;
    }
}
