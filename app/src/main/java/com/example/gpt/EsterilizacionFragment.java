package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class EsterilizacionFragment extends Fragment {
    private UUID campañaId;
    private EsterilizacionStorage mEsterilizacionStorage;
    private EsterilizacionAdapter mAdapter;
    private RecyclerView mEsterilizacionesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("Esterilizaciones");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.esterilizaciones_fragment, null);
        campañaId = (UUID) getArguments().getSerializable("ARG_CAMPAÑA_ID");
        return v;
    }

    private void updateUI (){
        mEsterilizacionStorage= EsterilizacionStorage.get(getActivity());
        List<Esterilizacion> esterilizaciones = mEsterilizacionStorage.getmEsterilizaciones(campañaId);
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new EsterilizacionAdapter(esterilizaciones);
            mEsterilizacionesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmEsterilizaciones(esterilizaciones);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
        }

        //updateSubtitle();

    }

    private class EsterilizacionesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView;
        private ImageView mGatoImageView, mPagoImageView;
        private Esterilizacion mEsterilizacion;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ListCampaña.class);
            intent.putExtra("ESTERILIZACION_ID", mEsterilizacion.getmIdEsterilizacion());
            startActivity(intent);
        }
        public EsterilizacionesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.campania_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombre_gato);
            mGatoImageView= itemView.findViewById(R.id.gato_foto);
            mPagoImageView= itemView.findViewById(R.id.esterilizacion_pagada);
            itemView.setOnClickListener(this);
        }

        public void bind (Esterilizacion esterilizacion, Gato mgato){
            mEsterilizacion= esterilizacion;
            if(mEsterilizacion.ismPagado()){
                mPagoImageView.setVisibility(View.VISIBLE);
            }
            mNombreTextView.setText(mgato.getmNombreGato());
        }
    }

    private class EsterilizacionAdapter extends RecyclerView.Adapter<EsterilizacionFragment.EsterilizacionesHolder>{
        private List<Esterilizacion> mEsterilizaciones;
        public EsterilizacionAdapter (List<Esterilizacion> esterilizaciones){
            mEsterilizaciones = esterilizaciones;
        }

        @NonNull
        @Override
        public EsterilizacionFragment.EsterilizacionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EsterilizacionesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EsterilizacionFragment.EsterilizacionesHolder holder, int position) {
            Esterilizacion esterilizacion = mEsterilizaciones.get(position);
            private CatLab mCatLab =CatLab.get(getActivity());
            private Gato mgato = mCatLab.getmGato(esterilizacion.getmIdGato());
            holder.bind(esterilizacion, mgato);
        }
        @Override
        public int getItemCount() {
            return mEsterilizaciones.size();
        }

        public void setmEsterilizaciones(List<Esterilizacion> esterilizaciones ){
            mEsterilizaciones = esterilizaciones;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.añadir_esterilizacion:




            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.esterilizaciones_menu,menu);
    }
}
