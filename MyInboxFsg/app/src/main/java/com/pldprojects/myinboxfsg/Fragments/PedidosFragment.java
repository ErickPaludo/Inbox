package com.pldprojects.myinboxfsg.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pldprojects.myinboxfsg.Fragments.Class.AppDatabase;
import com.pldprojects.myinboxfsg.Models.Caixas;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.Models.Pedidos;
import com.pldprojects.myinboxfsg.R;

import java.util.List;

public class PedidosFragment extends Fragment {
    LinearLayout layoutItem;
    LinearLayout layoutPed;
    Button btnCad;
    Button btnRecarrega;
    AppDatabase db;
    private static int notificationId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        layoutItem = view.findViewById(R.id.layoutItens);
        layoutPed = view.findViewById(R.id.layoutPedidos);
        btnCad = view.findViewById(R.id.buttonCriarPed);
        btnRecarrega = view.findViewById(R.id.buttonAtualizar);

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
        RetornaParaTabela();

        btnRecarrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetornaParaTabela();
            }
        });

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutPed.getChildCount() > 0)
                {
                    String itemPed = "";
                    for (int i = 0; i < layoutPed.getChildCount(); i++) {
                        View child = layoutPed.getChildAt(i);
                        int id = child.getId();
                        itemPed += id + "^";
                    }
                    db.Dao().inserirPedido(new Pedidos(itemPed));
                    RetornaParaTabela();
                    Toast.makeText(getContext(), "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    var itensped = db.Dao().listarTodosPedidos();
                    Pedidos p = itensped.get(itensped.size()-1);
                    mostrarNotificacao("Pedido Gerado com Sucesso!","Pedido n° " + p.id
                    + " foi criado.");
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

        List<Itens> itensdb = db.Dao().listarTodosItens();
        for (var obj : itensdb) {
            CriaBtnIten(obj);
        }
    }

    private void CriaBtnIten(Itens obj) {

        Button novoBotao = new Button(getContext());
        novoBotao.setId(obj.id);
        String valor = obj.toString();
        novoBotao.setText(valor);

        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        novoBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View btnToRemove = layoutItem.findViewById(obj.id);
                if (btnToRemove != null) {
                    layoutItem.removeView(btnToRemove);
                    CriaBtnPedido(obj);
                }
            }
        });

        layoutItem.addView(novoBotao);
    }

    private void CriaBtnPedido(Itens obj) {

        Button novoBotao = new Button(getContext());
        novoBotao.setId(obj.id);
        String valor = obj.toString();
        novoBotao.setText(valor);

        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        novoBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View btnToRemove = layoutPed.findViewById(obj.id);
                if (btnToRemove != null) {
                    layoutPed.removeView(btnToRemove);
                    CriaBtnIten(obj);
                }
            }
        });

        layoutPed.addView(novoBotao);
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