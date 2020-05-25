package com.example.gpt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class EsterilizacionActivity extends SingleFormularioFragment {
    private UUID campañaId;
    private Fragment fragment;
    private BottomNavigationView mBottomNavigationView;
    private EsterilizacionFragment mEsterilizacionFragment;
    private GatoFragment mGatoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGatoFragment = new GatoFragment();
        mEsterilizacionFragment = new EsterilizacionFragment();
        fragment = mGatoFragment;
        Bundle arguments = new Bundle();
        campañaId = (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        arguments.putSerializable("CAMPAÑA_ID", campañaId);
        fragment.setArguments(arguments);
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
    }

    private void menu (int menuItem) {
        switch (menuItem) {
            case R.id.gato_datos_esterilizacion:
                fragment = mGatoFragment;
                break;
            case R.id.esterilizacion_datos:
                fragment = mEsterilizacionFragment;
                break;
        }
        replaceFragment(fragment);
    }

    @Override
    protected Fragment createFragment() {
        return fragment;
    }
}
