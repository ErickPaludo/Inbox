package com.pldprojects.myinboxfsg.Models;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.pldprojects.myinboxfsg.Models.Itens;

@Entity
public class Pedidos {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String itens;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }

    public Pedidos(String itens) {
        this.itens = itens;
    }
}