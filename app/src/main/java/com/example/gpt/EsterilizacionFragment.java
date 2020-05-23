package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class EsterilizacionFragment extends Fragment {

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
        UUID campañaId = (UUID) getArguments().getSerializable("ARG_CAMPAÑA_ID");
        return v;
    }

    private class EsterilizacionesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView, mFechaTextView;
        private Esterilizacion mEsterilizacion;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ListCampaña.class);
            intent.putExtra("ESTERILIZACION_ID", mEsterilizacion.getmIdEsterilizacion());
            startActivity(intent);
        }
        public EsterilizacionesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.campania_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombre_campaña);
            mFechaTextView = itemView.findViewById(R.id.fecha_camapaña);
            itemView.setOnClickListener(this);
        }

        public void bind (Esterilizacion esterilizacion){
            mEsterilizacion= esterilizacion;

            //mFechaTextView.setText(campaña.getDateFormat().toString());
        }
    }




    /*private class CampañaAdapter extends RecyclerView.Adapter<EsterilizacionFragment.EsterilizacionesHolder>{
        private List<Campaña> mCampañas;
        public CampañaAdapter (List<Campaña> campañas){
            mCampañas = campañas;
        }

        @NonNull
        @Override
        public CampañaFragment.CampañasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CampañaFragment.CampañasHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CampañaFragment.CampañasHolder holder, int position) {
            //Toma los datos del crimen en la posicion indicada y los envia
            Campaña campaña = mCampañas.get(position);
            holder.bind(campaña);
        }
        @Override
        public int getItemCount() {
            return mCampañas.size();
        }

        public void setmCampañas(List<Campaña> campañas ){
            mCampañas = campañas;

        }

    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.esterilizaciones_menu,menu);
    }
}
