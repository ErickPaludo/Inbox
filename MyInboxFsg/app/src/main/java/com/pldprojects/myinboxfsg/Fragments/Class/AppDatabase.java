package com.pldprojects.myinboxfsg.Fragments.Class;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.pldprojects.myinboxfsg.Fragments.Class.DAO;
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;

@Database(entities = {Itens.class, Caixas.class, Pedidos.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAO Dao();
}