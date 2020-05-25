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
    UUID esterilizacionId;
    Fragment fragment;
    private BottomNavigationView mBottomNavigationView;
    private GatoFragment mGatoFragment;
    private EsterilizacionFragment mEsterilizacionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragment = new GatoFragment();
        esterilizacionId = (UUID) getIntent().getSerializableExtra("ESTERILIZACION_ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
    }
    @Override
    protected Fragment createFragment() {
        return fragment;
    }
}
