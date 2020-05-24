package com.example.gpt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class EsterilizacionActivity extends AppCompatActivity {

    UUID esterilizacionId;

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.homeFragment);



    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        esterilizacionId = (UUID) getIntent().getSerializableExtra("ESTERILIZACION_ID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esterilizacion_activity);
        fragment = new GatoFragment();
        fm.beginTransaction()
                .add(R.id.esterilizacion_container,fragment)
                .commit();

        mBottomNavigationView=findViewById(R.id.esterilizacion_menu);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menu(menuItem.getItemId());
                return true;
            }

        });

    }

    private void menu (int menuItem){
        switch (menuItem){
            case R.id.gato_datos_esterilizacion:
                fragment= new GatoFragment();
                break;
            case R.id.esterilizacion_datos:
                fragment=new EsterilizacionFragment();
                break;
        }
        if (fragment != null) {
            fm.beginTransaction()
                    .replace(R.id.esterilizacion_container, fragment)
                    .commit();
        }
    }
}
