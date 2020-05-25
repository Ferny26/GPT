package com.example.gpt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class Busqueda extends DialogFragment {
    private String  mTitle;
    private RecyclerView mBusquedaRecyclerView;
    private SearchView mSearchView;
    private CatLab mCatLab;
    private GatoAdapter mGatoAdapter;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.busqueda_dialog,null);
        mBusquedaRecyclerView = view.findViewById(R.id.busqueda_recyclerView);
        mSearchView = view.findViewById(R.id.buscador);
        mTitle = (String) getArguments().getSerializable("TITLE");
        if(mTitle.equals("Gato")){
            mCatLab = CatLab.get(getActivity());
            List<Gato> mGatos = mCatLab.getmGatos();
             mGatoAdapter = new GatoAdapter(getActivity(), mGatos);
            mBusquedaRecyclerView.setAdapter(mGatoAdapter);
        }else{

        }
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mTitle.equals("Gato")){
                    mGatoAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Buqueda " + mTitle)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }

    public void funcion (UUID id){

    }

}


