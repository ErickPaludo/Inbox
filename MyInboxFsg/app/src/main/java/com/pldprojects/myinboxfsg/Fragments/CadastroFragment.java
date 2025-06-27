package com.pldprojects.myinboxfsg.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pldprojects.myinboxfsg.Fragments.Class.AppDatabase;
import com.pldprojects.myinboxfsg.Fragments.Class.Util;
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CadastroFragment extends Fragment {
    LinearLayout layoutPage;
    LinearLayout layoutItem;
    EditText editNomeItem;
    EditText editTextAltura;
    EditText editTextLargura;
    EditText editTextComprimento;
    Button buttonCad;
    Spinner combo;
    Util util = new Util();
    Itens item;
    Caixas caixas;
    AppDatabase db;
    private static int notificationId = 0;



    boolean typecad = true;

    public CadastroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        layoutPage = view.findViewById(R.id.layoutItens);
        layoutItem = view.findViewById(R.id.layoutCampoItem);
        editNomeItem = view.findViewById(R.id.editNomeItem);
        editTextAltura = view.findViewById(R.id.editTextAltura);
        editTextLargura = view.findViewById(R.id.editTextLargura);
        editTextComprimento = view.findViewById(R.id.editTextComprimento);
        buttonCad = view.findViewById(R.id.buttonCad);
        combo = view.findViewById(R.id.combo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "canal_padrao",
                    "Canal Padrão",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
        db = Room.databaseBuilder(
                        requireContext(),
                        AppDatabase.class,
                        "meu_banco"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        List<String> itens = Arrays.asList("Cad. Itens", "Cad. Caixas");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), // ou getContext()
                android.R.layout.simple_spinner_item,
                itens
        );
        RetornaParaTabela();

        combo.setAdapter(adapter);
        combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelecionado = parent.getItemAtPosition(position).toString();
                LimpaTabela(); // sempre limpar antes

                if (itemSelecionado.equals("Cad. Itens")) {
                    typecad = true;
                    layoutItem.setVisibility(View.VISIBLE);
                } else {
                    typecad = false;
                    layoutItem.setVisibility(View.GONE);
                }
                RetornaParaTabela();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typecad = true;
            }
        });

        buttonCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetornaParaTabela();
                var listaValores = new ArrayList<String>();
                if (typecad) {
                    listaValores.add(editNomeItem.getText().toString());
                }
                listaValores.add(editTextAltura.getText().toString());
                listaValores.add(editTextLargura.getText().toString());
                listaValores.add(editTextComprimento.getText().toString());
                boolean valida = util.Valida(listaValores);
                if (valida) {
                    if (typecad) {
                        item = new Itens(editNomeItem.getText().toString(),
                                Double.parseDouble(editTextAltura.getText().toString()),
                                Double.parseDouble(editTextLargura.getText().toString()),
                                Double.parseDouble(editTextComprimento.getText().toString()));
                        db.Dao().inserirItem(item);
                        mostrarNotificacao("Item criado com Sucesso!","Item Cadastrado:" + item.toString());

                    } else {
                        caixas = new Caixas(Double.parseDouble(editTextAltura.getText().toString()),
                                Double.parseDouble(editTextLargura.getText().toString()),
                                Double.parseDouble(editTextComprimento.getText().toString()));
                        db.Dao().inserirCaixa(caixas);
                        mostrarNotificacao("Caixa cadastrada com Sucesso!","Caixa Cadastrada:" + caixas.toString());
                    }
                    CriaItemNatela();
                }
            }
        });

        return view;
    }

    private void CriaItemNatela() {
        Button novoBotao = new Button(getContext());
        novoBotao.setText(typecad ? item.toString() : caixas.toString());
        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutPage.addView(novoBotao);
        LimpaTela();
    }

    private void RetornaParaTabela() {
        LimpaTabela();
        if (typecad) {
            List<Itens> itensdb = db.Dao().listarTodosItens();
            for (var obj : itensdb) {
                CriaBtnItem(obj.toString());
            }
        } else {
            List<Caixas> caixasdb = db.Dao().listarTodasCaixas();
            for (var obj : caixasdb) {
                CriaBtnItem(obj.toString());
            }
        }

    }

    private void CriaBtnItem(String valor) {
        Button novoBotao = new Button(getContext());
        novoBotao.setText(valor);
        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutPage.addView(novoBotao);
    }

    private void LimpaTela() {
        editNomeItem.getText().clear();
        editTextAltura.getText().clear();
        editTextLargura.getText().clear();
        editTextComprimento.getText().clear();
    }

    private void LimpaTabela() {
        layoutPage.removeAllViews();
    }

    private void mostrarNotificacao(String tile,String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "canal_padrao")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(tile)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(notificationId++, builder.build());
    }
}