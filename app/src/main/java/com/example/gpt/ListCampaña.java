package com.example.gpt;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

//Clase encargada de iterar entre los fragmentos del botiquin y las esterilziaciones
public class ListCampaña extends SingleFragmentActivity {
    boolean mType;
    Fragment fragment;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Segun el tipo que sea de donde lo llame es el gragmento que va a crear y colocar
        mType=getIntent().getBooleanExtra("TYPE",false);
        UUID campañaId= (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        Bundle arguments = new Bundle();
        if(mType){
            fragment = new BotiquinCampañasFragment();
            arguments.putBoolean("TYPE",true);
            fragment.setArguments(arguments);

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
