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
        Bundle arguments = new Bundle();
        if (type){
            UUID gatoId = (UUID) getIntent().getSerializableExtra("GATO_ID");
            if(gatoId!=null){
                arguments.putSerializable("GATO_ID",gatoId);
                arguments.putBoolean("GATO",true);
            }else {
                arguments.putBoolean("GATO",false);
            }
            fragment = new GatoFragmentAdopciones();
            fragment.setArguments(arguments);
        }else{
            UUID adopcionId = (UUID) getIntent().getSerializableExtra("ADOPCION_ID");
            arguments.putSerializable("ADOPCION_ID",adopcionId);
            fragment = new VistaAdopcionFragment();
            fragment.setArguments(arguments);
        }

        return fragment;
    }
}
