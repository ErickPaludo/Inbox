package com.pldprojects.myinboxfsg.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pldprojects.myinboxfsg.Fragments.Class.Util;
import com.pldprojects.myinboxfsg.Models.Itens;
import com.pldprojects.myinboxfsg.R;

import java.util.ArrayList;

public class ItensFragment extends Fragment {
    LinearLayout layoutPage;
    EditText editNomeItem;
    EditText editTextAltura;
    EditText editTextLargura;
    EditText editTextComprimento;
    Button buttonCad;
    Util util = new Util();
    Itens item;
    public ItensFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itens, container, false);
        layoutPage = view.findViewById(R.id.layoutItens);
        editNomeItem = view.findViewById(R.id.editNomeItem);
        editTextAltura = view.findViewById(R.id.editTextAltura);
        editTextLargura = view.findViewById(R.id.editTextLargura);
        editTextComprimento = view.findViewById(R.id.editTextComprimento);
        buttonCad = view.findViewById(R.id.buttonCad);

        buttonCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var listaValores = new ArrayList<String>();
                listaValores.add(editNomeItem.getText().toString());
                listaValores.add(editTextAltura.getText().toString());
                listaValores.add(editTextLargura.getText().toString());
                listaValores.add(editTextComprimento.getText().toString());
               boolean valida = util.Valida(listaValores);
               if(valida){
                   item = new Itens(editNomeItem.getText().toString(),
                           Double.parseDouble(editTextAltura.getText().toString()),
                           Double.parseDouble(editTextLargura.getText().toString()),
                           Double.parseDouble(editTextComprimento.getText().toString()));
                   CriaItemNatela();
               }
            }
        });
        return view;
    }
    private void CriaItemNatela(){
        Button novoBotao = new Button(getContext());
        novoBotao.setText(item.toString());
        novoBotao.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutPage.addView(novoBotao);
        LimpaTela();
    }
    private void LimpaTela(){
        editNomeItem.getText().clear();
        editTextAltura.getText().clear();
        editTextLargura.getText().clear();
        editTextComprimento.getText().clear();
    }
}