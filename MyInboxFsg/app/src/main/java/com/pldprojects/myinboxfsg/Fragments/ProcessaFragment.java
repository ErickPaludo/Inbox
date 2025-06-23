package com.pldprojects.myinboxfsg.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.pldprojects.myinboxfsg.Fragments.Class.AppDatabase;
import com.pldprojects.myinboxfsg.Fragments.Class.LeituraCodigoBarrasActivity;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;
import com.pldprojects.myinboxfsg.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ProcessaFragment extends Fragment {

    LinearLayout layoutItem;
    LinearLayout layoutPed;
    TextView numped;
    Button btnCad;
    Button btnRecarrega;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processa, container, false);
        layoutItem = view.findViewById(R.id.layoutItens);
        layoutPed = view.findViewById(R.id.layoutPedidos);
        btnCad = view.findViewById(R.id.buttonCriarPed);
        btnRecarrega = view.findViewById(R.id.buttonAtualizar);
        numped = view.findViewById(R.id.textNumPed);
        db = Room.databaseBuilder(
                        requireContext(),
                        AppDatabase.class,
                        "meu_banco"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        RetornaParaTabela();

        btnRecarrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetornaParaTabela();
            }
        });

        return view;
    }

    private void LimparTela() {
        layoutItem.removeAllViews();
        layoutPed.removeAllViews();
    }

    private void RetornaParaTabela() {
        LimparTela();

        List<Pedidos> pedidos = db.Dao().listarTodosPedidos();
        for (var obj : pedidos) {
            CriaBtnPed(obj);
        }
    }

    private void CriaBtnPed(Pedidos obj) {

        Button novoBotao = new Button(getContext());
        novoBotao.setId(obj.id);
        String valor = "Pedido n°:" + obj.id;
        novoBotao.setText(valor);

        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        novoBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CriaBtnItem(obj);
            }
        });

        layoutItem.addView(novoBotao);
    }

    private void CriaBtnItem(Pedidos obj) {

        numped.setText("N° Ped " + obj.id);

        Pedidos pedidos = db.Dao().buscarPedioPorId(obj.id);

        String[] separaid = obj.itens.split("\\^");

        List<Integer> itemids = new ArrayList<>();

        for (String parte : separaid) {
            itemids.add(Integer.parseInt(parte));
        }

        ArrayList<Itens> itens = new ArrayList<Itens>();

        for (var itemped : itemids) {
            itens.add(db.Dao().buscarItensPorId(itemped));
        }
        layoutPed.removeAllViews();
        for (var objitem : itens) {
            Button novoBotao = new Button(getContext());
            novoBotao.setId(obj.id);
            String valor = objitem.toString();
            novoBotao.setText(valor);

            novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            layoutPed.addView(novoBotao);
        }

    }
}
