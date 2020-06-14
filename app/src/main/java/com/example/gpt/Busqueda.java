package com.example.gpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Busqueda extends DialogFragment {
    private String  mTitle;
    private RecyclerView mBusquedaRecyclerView;
    private SearchView mSearchView;
    private CatLab mCatLab;
    private UUID gatoId;
    public static final String EXTRA_GATO_ID = "gatoId";
    private GatoAdapter mGatoAdapter;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.busqueda_dialog,null);
        mBusquedaRecyclerView = view.findViewById(R.id.busqueda_recyclerView);
        mSearchView = view.findViewById(R.id.buscador);
        mTitle = (String) getArguments().getSerializable("TITLE");
        mCatLab = CatLab.get(getActivity());
        List<Gato> mGatos = mCatLab.getmGatos();
        mGatoAdapter = new GatoAdapter(getActivity(), mGatos);
        mBusquedaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBusquedaRecyclerView.setAdapter(mGatoAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mGatoAdapter.getFilter().filter(newText);
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


    public class GatoAdapter extends RecyclerView.Adapter<Busqueda.GatoHolder> implements Filterable {

        private Context context;
        private List<Gato> mGatos, mGatosFull;
        private UUID gatoId;


        public GatoAdapter(Context context, List<Gato> gatos) {
            this.context = context;
            this.mGatos = gatos;
            this.mGatosFull = new ArrayList<>(gatos);
        }

        @NonNull
        @Override
        public Busqueda.GatoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            return new GatoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Busqueda.GatoHolder holder, int position) {
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

    class   GatoHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView mNombreTextView, mCelularTextView;
        private ImageView mGatoImageView;
        private Gato mGato;

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

        @Override
        public void onClick(View v) {
            gatoId = mGato.getmIdGato();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_GATO_ID, gatoId);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            Dialog dialog = getDialog();
            dialog.cancel();
        }
    }

    public static void gatoSeleccionadoId(UUID gatoId){

    }
}


