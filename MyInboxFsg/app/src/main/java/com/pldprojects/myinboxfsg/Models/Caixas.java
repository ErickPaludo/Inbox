package com.pldprojects.myinboxfsg.Models;

import androidx.annotation.NonNull;

public class Caixas extends Objeto {
    public Caixas(double altura, double largura, double comprimento) {
        super(altura, largura, comprimento);
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + getAltura() + " x " + getComprimento() + " x " + getLargura() + ")";
    }
}
