package com.example.gpt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

//Calse abstracta apra intercambiar los fragmento de las actividades
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.homeFragment);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(fragment==null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.homeFragment, fragment)
                    .commit();
        }
    }
    protected void  replaceFragment(Fragment fragment){
            fm.beginTransaction()
                    .replace(R.id.homeFragment, fragment)
                    .commit();
    }
}
