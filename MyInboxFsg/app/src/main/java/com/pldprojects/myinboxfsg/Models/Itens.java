package com.pldprojects.myinboxfsg.Models;

import androidx.annotation.NonNull;

import com.pldprojects.myinboxfsg.Fragments.Class.Processos;

public class Itens extends Objeto implements Processos<Itens> {
    private String nome;
    public Itens(String nome, double altura, double largura, double comprimento) {
        super(altura, largura, comprimento);
        this.nome = nome;
    }

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    @Override
    public boolean validaObjeto(Itens obj) {
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return getNome() + " (" + getAltura() + " x " + getComprimento() + " x " + getLargura() + ")";
    }
}
