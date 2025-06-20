package com.pldprojects.myinboxfsg.Models;

public abstract class Objeto extends Dimensoes{
    private int id;


    public Objeto(){}

    public Objeto(double altura,double largura,double comprimento){
        super(altura,largura,comprimento);
    }


    public int getId(){return id;}
    public void setId(){ this.id = id;}


}
