package com.pldprojects.myinboxfsg.Fragments.Class;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    void inserirItem(Itens item);

    @Query("SELECT * FROM Itens")
    List<Itens> listarTodosItens();
    @Insert
    void inserirCaixa(Caixas caixas);

    @Query("SELECT * FROM Caixas")
    List<Caixas> listarTodasCaixas();
    @Insert
    void inserirPedido(Pedidos pedidos);

    @Query("SELECT * FROM Pedidos")
    List<Pedidos> listarTodosPedidos();

    @Query("SELECT * FROM Pedidos WHERE id = :id")
    Pedidos buscarPedioPorId(int id);

    @Query("SELECT * FROM Itens WHERE id = :id")
    Itens buscarItensPorId(int id);

}