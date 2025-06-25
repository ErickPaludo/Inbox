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
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;
import com.pldprojects.myinboxfsg.PedBoxActivity;
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
    Button buttonSelecionaCaixa;
    AppDatabase db;


    int ped = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processa, container, false);
        layoutItem = view.findViewById(R.id.layoutItens);
        layoutPed = view.findViewById(R.id.layoutPedidos);
        buttonSelecionaCaixa = view.findViewById(R.id.buttonSelecionaCaixa);
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

        buttonSelecionaCaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutItem.getChildCount() > 0) {
                    Pedidos pedidos = db.Dao().buscarPedioPorId(ped);

                    Intent intent = new Intent(getActivity(), PedBoxActivity.class);
                    intent.putExtra("pedidos", pedidos);

                    ArrayList<Integer> itensid = new ArrayList<>(ItensPed(pedidos));
                    ArrayList<Itens> listitens = new ArrayList<>();

                    for (var obj : itensid) {
                        listitens.add(db.Dao().buscarItensPorId(obj));
                    }

                    intent.putExtra("listitens", listitens);

                    ArrayList<Caixas> listaCaixas = new ArrayList<>();
                    listaCaixas = (ArrayList<Caixas>) db.Dao().listarTodasCaixas();
                    intent.putExtra("listacaixas", listaCaixas);

                    startActivity(intent);
                }
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
        ped = obj.id;
        numped.setText("N° Ped " + obj.id);

        ArrayList<Itens> itens = new ArrayList<Itens>();

        List<Integer> itemids = ItensPed(obj);
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

    private List<Integer> ItensPed(Pedidos pedItens) {

        String[] separaid = pedItens.itens.split("\\^");

        List<Integer> itemids = new ArrayList<>();

        for (String parte : separaid) {
            itemids.add(Integer.parseInt(parte));
        }
        return itemids;
    }
}
