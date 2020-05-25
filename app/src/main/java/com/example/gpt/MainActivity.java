package com.example.gpt;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends SingleFragmentActivity{

    private BottomNavigationView mBottomNavigationView;

    Fragment fragment;
    CampañaFragment mCampañaFragment = new CampañaFragment();
    AdopcionFragment mAdopcionFragment = new AdopcionFragment();
    PensionFragment mPensionFragment = new PensionFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragment = mCampañaFragment;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView=findViewById(R.id.HomeNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menu(menuItem.getItemId());
                return true;
            }

        });
    }
    @Override
    protected Fragment createFragment() {
        return fragment;
    }

    private void menu (int menuItem){
        switch (menuItem){
            case R.id.campaña:
                fragment= mCampañaFragment;
                break;
            case R.id.adopcion:
                fragment= mAdopcionFragment;
                break;
            case R.id.capital:
                break;
            case R.id.pension:
                fragment= mPensionFragment;
                break;
        }
        replaceFragment(fragment);
    }

}
