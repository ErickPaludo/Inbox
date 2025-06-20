package com.pldprojects.myinboxfsg.Models;

public abstract class Objeto extends Dimensoes {

    public int id;

    public Objeto() {}

    public Objeto(double altura, double largura, double comprimento) {
        super(altura, largura, comprimento);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
