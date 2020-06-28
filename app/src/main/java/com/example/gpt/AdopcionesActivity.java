package com.example.gpt;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class AdopcionesActivity extends SingleFormularioFragment{
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
        if (type){
            fragment = new GatoFragmentAdopciones();
        }else{
            Bundle arguments = new Bundle();
            UUID adopcion_id = (UUID) getIntent().getSerializableExtra("ADOPCION_ID");
            fragment = new VistaAdopcionFragment();
            fragment.setArguments(arguments);
        }

        return fragment;
    }
}
