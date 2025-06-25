package com.pldprojects.myinboxfsg.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Caixas extends Objeto implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Caixas() {
        // Construtor vazio obrigatório para o Room
    }

    public Caixas(double altura, double largura, double comprimento) {
        super(altura, largura, comprimento);
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + getAltura() + " x " + getComprimento() + " x " + getLargura() + ")";
    }
}
