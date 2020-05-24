package com.example.gpt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class ListCampaña extends AppCompatActivity {
    boolean mType;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.campaña_list_fragment);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_campania);
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

        fm.beginTransaction()
                .add(R.id.campaña_list_fragment,fragment)
                .commit();
    }

}
