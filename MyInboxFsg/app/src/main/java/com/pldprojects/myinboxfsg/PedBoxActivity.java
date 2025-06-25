package com.pldprojects.myinboxfsg;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Dimensoes;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedBoxActivity extends AppCompatActivity {
    Pedidos pedidos;

    ArrayList<Itens> listItens = new ArrayList<>();

    ArrayList<Caixas> listcaixas = new ArrayList<>();

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
        ArrayList<Caixas> caixasUsadas = new ArrayList<>();

        for (var produto : listItens) {
            Caixas caixa = encontrarMelhorCaixa(produto);
            if (caixa != null) {
                System.out.println("Produto " + produto.id + " cabe na " + caixa.toString());
                caixasUsadas.add(caixa);
            } else {
                System.out.println("Produto " + produto.id + " NÃO cabe em nenhuma caixa");
            }
        }
    }

    public Caixas cabeNaCaixa(Itens itens) {
        List<Dimensoes> rotacoes = gerarRotacoes(itens);
        for (var cx : listcaixas) {
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