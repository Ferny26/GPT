package com.example.gpt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFormularioFragment extends AppCompatActivity {
    protected abstract Fragment createFragment();
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.formulario_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_formulario_activity);
        if(fragment==null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.formulario_container, fragment)
                    .commit();
        }
    }
    protected void  replaceFragment(Fragment fragment){

        fm.beginTransaction()
                .replace(R.id.formulario_container, fragment)
                .commit();
    }
}
