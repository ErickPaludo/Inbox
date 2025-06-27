package com.pldprojects.myinboxfsg;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pldprojects.myinboxfsg.Models.CaixaComItens;
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Dimensoes;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedBoxActivity extends AppCompatActivity {

    Pedidos pedidos;

    ArrayList<Itens> listItens = new ArrayList<>();
    ArrayList<Caixas> listcaixas = new ArrayList<>();
    ArrayList<CaixaComItens> caixasUsadasItens = new ArrayList<>();
    ListView listaitensview;

    ArrayList<String> listaitensviewtostring = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ped_box);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pedidos = (Pedidos) getIntent().getSerializableExtra("pedidos");
        listItens = (ArrayList<Itens>) getIntent().getSerializableExtra("listitens");
        listcaixas = (ArrayList<Caixas>) getIntent().getSerializableExtra("listacaixas");

        listaitensview = findViewById(R.id.listViewPedidos);

        // Adaptador ligado à lista
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaitensviewtostring);
        listaitensview.setAdapter(adapter);

          for (Itens produto : listItens) {
              Caixas caixa = encontrarMelhorCaixa(produto);
              if (caixa != null) {
                 // listcaixas.add(caixa);
                  caixasUsadasItens.add(new CaixaComItens(caixa, produto));
                //  listaitensviewtostring.add("Item - " + produto.toString() + "\nCaixa - " + caixa);
              } else {
                  listaitensviewtostring.add("Item - " + produto.toString() + "\nCaixa - Sem Caixa");
              }
          }
          String msggroup = "";

        for (var caixas : listcaixas) {
            for (var itens : caixasUsadasItens) {
                if (caixas.id == itens.getCaixa().id) {
                    msggroup += msggroup.equals("") ? "Caixa: " + caixas.toString() + "\nItens: " : "";
                    msggroup += itens.getItens().toString() + "\n";
                }
            }
            if (!msggroup.equals("")) {
                listaitensviewtostring.add(msggroup);
                msggroup = "";
            }
        }
        adapter.notifyDataSetChanged();
    }

    public Caixas cabeNaCaixa(Itens itens) {
        List<Dimensoes> rotacoes = gerarRotacoes(itens);
        for (Caixas cx : listcaixas) {
            for (Dimensoes itemRotacionado : rotacoes) {
                if (itemRotacionado.getAltura() <= cx.getAltura() &&
                        itemRotacionado.getLargura() <= cx.getLargura() &&
                        itemRotacionado.getComprimento() <= cx.getComprimento()) {
                    return cx;
                }
            }
        }
        return null;
    }

    public List<Dimensoes> gerarRotacoes(Dimensoes dim) {
        return Arrays.asList(
                new Dimensoes(dim.getAltura(), dim.getLargura(), dim.getComprimento()),
                new Dimensoes(dim.getAltura(), dim.getComprimento(), dim.getLargura()),
                new Dimensoes(dim.getLargura(), dim.getAltura(), dim.getComprimento()),
                new Dimensoes(dim.getLargura(), dim.getComprimento(), dim.getAltura()),
                new Dimensoes(dim.getComprimento(), dim.getAltura(), dim.getLargura()),
                new Dimensoes(dim.getComprimento(), dim.getLargura(), dim.getAltura())
        );
    }

    public Caixas encontrarMelhorCaixa(Itens produto) {
        return cabeNaCaixa(produto);
    }
}
