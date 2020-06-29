package com.example.gpt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class PensionActivity  extends  SingleFormularioFragment{
    private UUID pensionId;
    private Fragment fragment;
    private BottomNavigationView mBottomNavigationView;
    private PensionDatosFragment mPensionDatosFragment;
    private MenuItem mMenuItem;
    private boolean type;
    Bundle arguments = new Bundle();
    private PensionGatoFragment mPensionGatoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        type = getIntent().getBooleanExtra("TYPE",false);

            mPensionGatoFragment = new PensionGatoFragment();
            mPensionDatosFragment = new PensionDatosFragment();
            fragment = mPensionGatoFragment;
            pensionId = (UUID) getIntent().getSerializableExtra("PENSION_ID");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.universal_formulario_activity);
            mBottomNavigationView=findViewById(R.id.esterilizacion_menu);
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    menu(menuItem.getItemId());
                    return true;
                }
            });
            Menu menu = mBottomNavigationView.getMenu();
            mMenuItem = menu.getItem(1);
            mMenuItem.setTitle("Datos Pension");
      
    }

    private void menu (int menuItem) {
        switch (menuItem) {
            case R.id.gato_datos_esterilizacion:
                fragment = mPensionGatoFragment;
                if(pensionId == null){
                    arguments.putBoolean("NUEVA_INSTANCIA", true);
                }
                else{
                    arguments.putSerializable("PENSION_ID",pensionId);
                }
                fragment.setArguments(arguments);
                break;
            case R.id.esterilizacion_datos:
                fragment = mPensionDatosFragment;
                arguments.putSerializable("PENSION_ID", pensionId);
                fragment.setArguments(arguments);
                break;
        }
        replaceFragment(fragment);
    }

    @Override
    protected Fragment createFragment() {
        if(pensionId == null){
            arguments.putBoolean("NUEVA_INSTANCIA", true);
        }
        else{
            arguments.putSerializable("PENSION_ID", pensionId);
        }
        fragment.setArguments(arguments);
        return fragment;
    }
}
