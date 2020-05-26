package com.example.gpt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GatoAdapter extends RecyclerView.Adapter<GatoAdapter.GatoHolder> implements Filterable {

    private Context context;
    private List<Gato> mGatos, mGatosFull;

    class   GatoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView, mCelularTextView;
        private ImageView mGatoImageView;
        private Gato mGato;
        @Override
        public void onClick(View v) {
            UUID gatoId = mGato.getmIdGato();

        }
        public GatoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.busqueda_list, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombre);
            mGatoImageView = itemView.findViewById(R.id.image_gato);
            mCelularTextView = itemView.findViewById(R.id.celular);
            itemView.setOnClickListener(this);
        }

        public void bind (Gato gato){
            mGato = gato;
            mNombreTextView.setText(mGato.getmNombreGato());
            mGatoImageView.setImageResource(R.drawable.gato_gris);
            mCelularTextView.setVisibility(View.GONE);

        }

    }

    public GatoAdapter(Context context, List<Gato> gatos) {
        this.context = context;
        this.mGatos = gatos;
        this.mGatosFull = new ArrayList<>(gatos);
    }

    @NonNull
    @Override
    public GatoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new GatoHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull GatoHolder holder, int position) {
        Gato gato = mGatos.get(position);
        holder.bind(gato);
    }

    @Override
    public int getItemCount() {
        return mGatos.size();
    }

    @Override
    public Filter getFilter() {
        return gatoFilter;
    }

    private Filter gatoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Gato> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(mGatosFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Gato item : mGatosFull) {
                    if (item.getmNombreGato().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mGatos.clear();
            mGatos.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
