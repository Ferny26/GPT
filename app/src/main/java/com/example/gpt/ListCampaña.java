package com.example.gpt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class ListCampaña extends AppCompatActivity {

    final static String EXTRA_CAMPAÑA_ID = "CampañaId";
    boolean mType;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.campaña_list_fragment);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_campania);
        mType=getIntent().getBooleanExtra("TYPE",false);
        UUID campañaId= (UUID) getIntent().getSerializableExtra(EXTRA_CAMPAÑA_ID);
        if(mType){
            fragment = new BotiquinCampañaFragment();
        }else{
            fragment = new EsterilizacionFragment();
        }


        fm.beginTransaction()
                .add(R.id.campaña_list_fragment,fragment)
                .commit();
    }

    public static Intent newIntent(Context packageContext, UUID campañaId, int type){
        Intent intent = new Intent(packageContext,ListCampaña.class);
        intent.putExtra(EXTRA_CAMPAÑA_ID, campañaId);
        return intent;
    }
}
