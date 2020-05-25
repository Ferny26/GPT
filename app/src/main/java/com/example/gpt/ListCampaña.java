package com.example.gpt;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class ListCampaña extends SingleFragmentActivity {
    boolean mType;
    Fragment fragment;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mType=getIntent().getBooleanExtra("TYPE",false);
        UUID campañaId= (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        Bundle arguments = new Bundle();
        if(mType){
            fragment = new BotiquinCampañaFragment();

        }else{
            fragment = new EsterilizacionesFragment();
            arguments.putSerializable ("ARG_CAMPAÑA_ID",campañaId);
            fragment.setArguments(arguments);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.HomeNavigationView);
        mBottomNavigationView.setVisibility(View.GONE);
    }
    @Override
    protected Fragment createFragment() {
        return fragment;
    }
}
