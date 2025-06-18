package com.pldprojects.myinboxfsg.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pldprojects.myinboxfsg.LeituraCodigoBarrasActivity;
import com.pldprojects.myinboxfsg.R;

import org.w3c.dom.Text;

public class ProcessaFragment extends Fragment {

    private Button btnProcess;
    private TextView textNumberPed;

    // ⬇️ Substituto moderno do startActivityForResult()
    private final ActivityResultLauncher<Intent> scannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    String codigoBarras = result.getData().getStringExtra("SCAN_RESULT");
                    String formatoCodigo = result.getData().getStringExtra("SCAN_RESULT_FORMAT");

                    if (!TextUtils.isEmpty(codigoBarras)) {
                        textNumberPed.setText(codigoBarras);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processa, container, false);
        btnProcess = view.findViewById(R.id.buttonProcessa);
        textNumberPed = view.findViewById(R.id.textNumberPed);
        btnProcess.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LeituraCodigoBarrasActivity.class);
            scannerLauncher.launch(intent); // ⬅️ Chamada atual
        });

        return view;
    }
}
