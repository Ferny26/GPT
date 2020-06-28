package com.example.gpt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.UUID;

public class MaterialCampañaActivity extends SingleFragmentActivity {
    Fragment fragment;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected Fragment createFragment() {
        return fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UUID campañaId= (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        Bundle arguments = new Bundle();

        fragment = new MaterialCampañaFragment();
        arguments.putSerializable ("ARG_CAMPAÑA_ID",campañaId);
        fragment.setArguments(arguments);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.HomeNavigationView);
        mBottomNavigationView.setVisibility(View.GONE);
    }

}
