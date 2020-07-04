package com.example.gpt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;


//Case que hereda de un formulario abstracto para poder iterar de manera eficiente entre fragmentos
public class EsterilizacionActivity extends SingleFormularioFragment {
    private UUID campañaId, esterilizacionId;
    private Fragment fragment;
    private EsterilizacionFragment mEsterilizacionFragment;
    Bundle arguments = new Bundle();
    private GatoFragment mGatoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se crea 2 fragmentos los cuales se estaran intercambiando dependiendo de la opcion del menú seleccionada
        mGatoFragment = new GatoFragment();
        mEsterilizacionFragment = new EsterilizacionFragment();
        fragment = mGatoFragment;
        campañaId = (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        esterilizacionId = (UUID) getIntent().getSerializableExtra("ESTERILIZACION_ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
        BottomNavigationView mBottomNavigationView = findViewById(R.id.esterilizacion_menu);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menu(menuItem.getItemId());
                return true;
            }
        });
    }

    //Dependiendo del caso, se iterará el fragmento, asi como si fue enviado por medio de uno que ya este creado, mandará el id de dicha esterilizacion para poder colocar los datos correspondietnes
    private void menu (int menuItem) {
        switch (menuItem) {
            case R.id.gato_datos_esterilizacion:
                fragment = mGatoFragment;
                if(esterilizacionId == null){
                    arguments.putBoolean("NUEVA_INSTANCIA", true);
                }
                else{
                    arguments.putSerializable("ESTERILIZACION_ID", esterilizacionId);
                }
                fragment.setArguments(arguments);
                break;
            case R.id.esterilizacion_datos:
                fragment = mEsterilizacionFragment;
                arguments.putSerializable("CAMPAÑA_ID", campañaId);
                fragment.setArguments(arguments);
                break;
        }
        replaceFragment(fragment);
    }

    @Override
    //Se colocan los respectivos datos y se retorna el fragmneto
    protected Fragment createFragment() {
        if(esterilizacionId == null){
            arguments.putBoolean("NUEVA_INSTANCIA", true);
        }
        else{
            arguments.putSerializable("ESTERILIZACION_ID", esterilizacionId);
        }
        fragment.setArguments(arguments);
        return fragment;
    }
}
