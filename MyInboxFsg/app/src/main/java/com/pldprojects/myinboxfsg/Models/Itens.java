package com.pldprojects.myinboxfsg.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Itens extends Objeto {

    @PrimaryKey(autoGenerate = true)
    public int id; // necessário em toda @Entity

    private String nome;

    public Itens() {
        // Construtor vazio exigido pelo Room
    }

    public Itens(String nome, double altura, double largura, double comprimento) {
        super(altura, largura, comprimento);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NonNull
    @Override
    public String toString() {
        return getNome() + " (" + getAltura() + " x " + getComprimento() + " x " + getLargura() + ")";
    }
}
