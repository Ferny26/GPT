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
    UUID campañaId;
    Fragment fragment;
    private BottomNavigationView mBottomNavigationView;
    private EsterilizacionFragment mEsterilizacionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragment = new GatoFragment();
        Bundle arguments = new Bundle();
        campañaId = (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        arguments.putSerializable("CAMPAÑA_ID", campañaId);
        fragment.setArguments(arguments);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
        super.onBackPressed();
    }

    @Override
    protected Fragment createFragment() {
        return fragment;
    }
}
