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
import com.pldprojects.myinboxfsg.R;

import org.w3c.dom.Text;

import java.util.List;

public class ProcessaFragment extends Fragment {

    LinearLayout layoutItem;
    LinearLayout layoutPed;
    Button btnCad;
    Button btnRecarrega;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        layoutItem = view.findViewById(R.id.layoutItens);
        layoutPed = view.findViewById(R.id.layoutPedidos);
        btnCad = view.findViewById(R.id.buttonCriarPed);
        btnRecarrega = view.findViewById(R.id.buttonAtualizar);

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
  // private Button btnProcess;
  // private TextView textNumberPed;
 //   private final ActivityResultLauncher<Intent> scannerLauncher = registerForActivityResult(
 //           new ActivityResultContracts.StartActivityForResult(),
 //           result -> {
 //               if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
 //                   String codigoBarras = result.getData().getStringExtra("SCAN_RESULT");
 //                   String formatoCodigo = result.getData().getStringExtra("SCAN_RESULT_FORMAT");
//
 //                   if (!TextUtils.isEmpty(codigoBarras)) {
 //                       textNumberPed.setText(codigoBarras);
 //                   }
 //               }
 //           }
 //   );
//
 //   @Nullable
 //   @Override
 //   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
 //                            @Nullable Bundle savedInstanceState) {
 //       View view = inflater.inflate(R.layout.fragment_processa, container, false);
 //       btnProcess = view.findViewById(R.id.buttonProcessa);
 //       textNumberPed = view.findViewById(R.id.textNumberPed);
 //       btnProcess.setOnClickListener(v -> {
 //           Intent intent = new Intent(getActivity(), LeituraCodigoBarrasActivity.class);
 //           scannerLauncher.launch(intent); // ⬅️ Chamada atual
 //       });
//
 //       LinearLayout layout = view.findViewById(R.id.layoutTeste); // Um layout definido no XML
//
 //       for (int i = 0; i < 5; i++) {
 //           TextView textView = new TextView(getActivity());
 //           textView.setText("Texto " + (i + 1));
 //           textView.setTextSize(18);
 //           textView.setPadding(20, 20, 20, 20);
//
 //           layout.addView(textView); // Adiciona no layout
 //       }
//
 //       return view;
 //   }
}
