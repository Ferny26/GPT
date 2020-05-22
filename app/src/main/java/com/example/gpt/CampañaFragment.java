package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;


public class CampañaFragment extends Fragment {

    private static final int REQUEST_CREATE = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.campanas_fragment,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.campa_as);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.campa_a_menu, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CREATE) {
            Date date = (Date) data.getSerializableExtra(CrearCampañaDialog.EXTRA_DATE);
            String nombreCampaña = (String) data.getSerializableExtra(CrearCampañaDialog.EXTRA_NAME);
            Campaña campaña = new Campaña(nombreCampaña, date);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.añadir:
                FragmentManager manager = getFragmentManager();
                CrearCampañaDialog dialog = new CrearCampañaDialog();
                dialog.setTargetFragment(CampañaFragment.this, REQUEST_CREATE);
                dialog.show(manager,DIALOG_CREATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }



    }
}
