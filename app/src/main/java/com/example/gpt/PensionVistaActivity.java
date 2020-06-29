package com.example.gpt;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class PensionVistaActivity extends SingleFormularioFragment {

    private Fragment fragment;
    private Boolean type;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        type = getIntent().getBooleanExtra("TYPE",false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
        mBottomNavigationView=findViewById(R.id.esterilizacion_menu);
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    protected Fragment createFragment() {
        Bundle arguments = new Bundle();
        if (type){
            UUID pension_id = (UUID) getIntent().getSerializableExtra("PENSION_ID");
            fragment = new VistaPensionFragment();
            arguments.putSerializable("PENSION_ID",pension_id);
            arguments.putBoolean("GATO",false);
            fragment.setArguments(arguments);
        }else{
            fragment = new BotiquinCampañasFragment();
            arguments.putBoolean("TYPE",false);
            fragment.setArguments(arguments);
        }

        return fragment;
    }


}
