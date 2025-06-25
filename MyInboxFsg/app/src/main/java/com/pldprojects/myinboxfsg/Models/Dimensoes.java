package com.pldprojects.myinboxfsg.Models;

import java.io.Serializable;

public class Dimensoes implements Serializable {
    private double altura;
    private double largura;
    private double comprimento;

    public Dimensoes(){
    }

    public Dimensoes(double altura,double largura,double comprimento){
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
    }
    public double getAltura(){return altura;}
    public double getLargura(){return largura;}
    public double getComprimento(){return comprimento;}

    public void setAltura(double altura){this.altura = altura;}
    public void setLargura(double largura){this.largura = largura;}
    public void setComprimento(double comprimento){this.comprimento = comprimento;}
}
