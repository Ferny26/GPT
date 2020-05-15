package com.example.gpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.homeFragment);
    ConstraintLayout mConstraintLayout;


    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new CampañaFragment();
                fm.beginTransaction()
                        .add(R.id.homeFragment,fragment)
                        .commit();

        mBottomNavigationView=findViewById(R.id.HomeNavigationView);
        mConstraintLayout=findViewById(R.id.homeLayout);

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
            case R.id.campaña:
                fragment= new CampañaFragment();
                break;
            case R.id.adopcion:
                fragment=new AdopcionFragment();
                break;
            case R.id.capital:
                break;
            case R.id.pension:
                fragment= new PensionFragment();
                break;
        }
        if (fragment != null) {
            fm.beginTransaction()
            .replace(R.id.homeFragment, fragment)
                    .commit();
        }
    }
}
