package com.pldprojects.myinboxfsg.Fragments;

import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.pldprojects.myinboxfsg.Fragments.Class.AppDatabase;
import com.pldprojects.myinboxfsg.Fragments.Class.LeituraCodigoBarrasActivity;
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;
import com.pldprojects.myinboxfsg.PedBoxActivity;
import com.pldprojects.myinboxfsg.R;

import java.util.ArrayList;
import java.util.List;

public class ProcessaFragment extends Fragment {

    LinearLayout layoutItem;
    LinearLayout layoutPed;
    TextView numped;
    Button btnCad;
    Button btnRecarrega;
    Button buttonSelecionaCaixa;
    Button buttonScanner;
    AppDatabase db;
    private TextView textNumberPed;
    int ped = 0;

    private final ActivityResultLauncher<Intent> scannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String formatoCodigo = "";
                    try {
                        formatoCodigo = data.getStringExtra("SCAN_RESULT_FORMAT");
                    } catch (Exception e) {
                        e.printStackTrace(); // Loga o erro
                    }

                    if (!TextUtils.isEmpty(formatoCodigo)) {
                        try {
                            ped = Integer.valueOf(data.getStringExtra("SCAN_RESULT"));

                            ChamaProcessamento();
                        } catch (Exception ex) {
                            ExibeMsg("Erro", "Cod inválido: " + data.getStringExtra("SCAN_RESULT"));

                            RetornaParaTabela();
                        }
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processa, container, false);

        layoutItem = view.findViewById(R.id.layoutItens);
        layoutPed = view.findViewById(R.id.layoutPedidos);
        buttonSelecionaCaixa = view.findViewById(R.id.buttonSelecionaCaixa);
        btnRecarrega = view.findViewById(R.id.buttonAtualizar);
        numped = view.findViewById(R.id.textNumPed);
        buttonScanner = view.findViewById(R.id.buttonScanner);
        textNumberPed = view.findViewById(R.id.textNumPed); // Referência para o TextView de exibição do código

        db = Room.databaseBuilder(
                        requireContext(),
                        AppDatabase.class,
                        "meu_banco"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        RetornaParaTabela();

        buttonScanner.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LeituraCodigoBarrasActivity.class);
            scannerLauncher.launch(intent);
        });

        btnRecarrega.setOnClickListener(v -> RetornaParaTabela());

        buttonSelecionaCaixa.setOnClickListener(v -> {
            if (layoutItem.getChildCount() > 0) {
                ChamaProcessamento();
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
        ped = 0;

        textNumberPed.setText("N° Ped");
        List<Pedidos> pedidos = db.Dao().listarTodosPedidos();
        for (Pedidos obj : pedidos) {
            CriaBtnPed(obj);
        }
    }

    private void CriaBtnPed(Pedidos obj) {
        Button novoBotao = new Button(getContext());
        novoBotao.setId(obj.id);
        String valor = "Pedido n°: " + obj.id;
        novoBotao.setText(valor);

        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        novoBotao.setOnClickListener(v -> CriaBtnItem(obj));
        layoutItem.addView(novoBotao);
    }

    private void CriaBtnItem(Pedidos obj) {
        ped = obj.id;
        numped.setText("N° Ped " + obj.id);

        List<Integer> itemids = ItensPed(obj);
        List<Itens> itens = new ArrayList<>();

        for (int itemped : itemids) {
            itens.add(db.Dao().buscarItensPorId(itemped));
        }

        layoutPed.removeAllViews();

        for (Itens objitem : itens) {
            Button novoBotao = new Button(getContext());
            novoBotao.setId(objitem.id); // Corrigido: botão com ID do item
            novoBotao.setText(objitem.toString());
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
            try {
                itemids.add(Integer.parseInt(parte));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return itemids;
    }

    private void ChamaProcessamento() {
        Pedidos pedidos = db.Dao().buscarPedioPorId(ped);
        if (pedidos != null) {
            Intent intent = new Intent(getActivity(), PedBoxActivity.class);
            intent.putExtra("pedidos", pedidos);

            ArrayList<Integer> itensid = new ArrayList<>(ItensPed(pedidos));
            ArrayList<Itens> listitens = new ArrayList<>();

            for (int id : itensid) {
                listitens.add(db.Dao().buscarItensPorId(id));
            }

            intent.putExtra("listitens", listitens);

            ArrayList<Caixas> listaCaixas = new ArrayList<>(db.Dao().listarTodasCaixas());
            intent.putExtra("listacaixas", listaCaixas);

            startActivity(intent);

        } else {
            if(ped != 0) {
                ExibeMsg("Erro", "Pedido não Identificado");
                RetornaParaTabela();
            }
        }
    }

    private void ExibeMsg(String title, String msg) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}
