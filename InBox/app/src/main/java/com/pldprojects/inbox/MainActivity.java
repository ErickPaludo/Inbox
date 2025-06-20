package com.pldprojects.inbox;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pldprojects.inbox.Fragments.ItensFragment;
import com.pldprojects.inbox.Fragments.CaixasFragment;
import com.pldprojects.inbox.Fragments.PedidosFragment;
import com.pldprojects.inbox.Fragments.PedidosProcessadosFragment;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private Button  buttonProcessar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);
        buttonProcessar = (Button)findViewById(R.id.buttonProcessa);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Itens", ItensFragment.class)
                .add("Caixas", CaixasFragment.class)
                .add("Pedidos", PedidosFragment.class)
                .add("Processados", PedidosProcessadosFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}