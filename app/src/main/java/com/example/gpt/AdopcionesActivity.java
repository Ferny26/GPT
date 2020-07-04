package com.example.gpt;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class AdopcionesActivity extends SingleFormularioFragment{

    //Recibe un intent por parte de 2 diferentes actividades, dependiendo del intent es el tipo de fragmento que instancia y la cantidad de argumentos que enviará
    private Boolean type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = getIntent().getBooleanExtra("TYPE",false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
        BottomNavigationView mBottomNavigationView = findViewById(R.id.esterilizacion_menu);
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    protected Fragment createFragment() {
        Bundle arguments = new Bundle();
        Fragment fragment;
        //Si el tipo es verdadero, creará una vista para poder crear un nuevo registro, inidicando que no se selecciono una adopcion ya existente
        //Si es faslo, por lo tanto elegieron una adopcion ya existente, por lo que se manda dicho fragment para poder registrar un adoptante
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
