package com.example.gpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Busqueda extends DialogFragment {
    private String  mTitle, query;
    private RecyclerView mBusquedaRecyclerView;
    private SearchView mSearchView;
    private CatLab mCatLab;
    private PersonaStorage mPersonaStorage;
    private UUID personaId;
    private UUID gatoId;
    public static final String EXTRA_GATO_ID = "gatoId";
    public static final String EXTRA_PERSONA_ID = "personaId";
    private GatoAdapter mGatoAdapter;
    private PersonaAdapter mPersonaAdapter;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.busqueda_dialog,null);
        mBusquedaRecyclerView = view.findViewById(R.id.busqueda_recyclerView);
        mSearchView = view.findViewById(R.id.buscador);
        mTitle = (String) getArguments().getSerializable("TITLE");
        if(mTitle == "Gato"){
            mCatLab = CatLab.get(getActivity());
            query =  (String) getArguments().getSerializable("QUERY");
            List<Gato> mGatos = mCatLab.getmBusquedaGatos(query);
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


        }else if(mTitle == "Responsable") {
            mPersonaStorage = PersonaStorage.get(getActivity());
            List<Persona> mPersonas = mPersonaStorage.getmPersonas();
            mPersonaAdapter = new PersonaAdapter(getActivity(), mPersonas);
            mBusquedaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBusquedaRecyclerView.setAdapter(mPersonaAdapter);

            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mPersonaAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }


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

    private void putImageView(Gato mGato, ImageView mGatoImage) {
        File mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            //Asignacion de orientacion correcta para la foto
            ExifInterface exif = null;
            exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            //obtiene la orientacion de la foto
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //Envia la orientacion y el bitmap como parametros para modificarlos
            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
            //Una vez adecuada la foto, se coloca en el imageView
            if(bmRotated!=null) {
                mGatoImage.setImageBitmap(bmRotated);
            }
        } catch (IOException e) {
            mGatoImage.setImageResource(R.drawable.gato_gris);
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        //Verifica la orientacion de la foto y asigna los parametros necesarios de escala y rotacion
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            //Crea un nuevo bitmap con los parametros correctos de orientacion de la foto y lo regresa
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
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
        private TextView mNombreTextView;
        private ImageView mGatoImageView;
        private Gato mGato;

        public GatoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.busqueda_list, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombre);
            mGatoImageView = itemView.findViewById(R.id.image_busqueda);
            itemView.setOnClickListener(this);
        }

        public void bind (Gato gato){
            mGato = gato;
            mNombreTextView.setText(mGato.getmNombreGato());
            putImageView(mGato, mGatoImageView);
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

    public class PersonaAdapter extends RecyclerView.Adapter<Busqueda.PersonaHolder> implements Filterable {

        private Context context;
        private List<Persona> mPersonas, mPersonasFull;
        private UUID personaId;


        public PersonaAdapter(Context context, List<Persona> personas) {
            this.context = context;
            this.mPersonas = personas;
            this.mPersonasFull = new ArrayList<>(personas);
        }

        @NonNull
        @Override
        public Busqueda.PersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            return new PersonaHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Busqueda.PersonaHolder holder, int position) {
            Persona persona = mPersonas.get(position);
            holder.bind(persona);
        }

        @Override
        public int getItemCount() {
            return mPersonas.size();
        }

        @Override
        public Filter getFilter() {
            return personaFilter;
        }

        private Filter personaFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Persona> filterList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filterList.addAll(mPersonasFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Persona item : mPersonasFull) {
                        if (item.getmNombre().toLowerCase().contains(filterPattern)) {
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
                mPersonas.clear();
                mPersonas.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    class PersonaHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView mNombreTextView, mCelularTextView;
        private ImageView mPersonaImageView;
        private Persona mPersona;

        public PersonaHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.busqueda_list, parent, false));
            mCelularTextView = itemView.findViewById(R.id.celular);
            mPersonaImageView = itemView.findViewById(R.id.image_busqueda);
            mNombreTextView = itemView.findViewById(R.id.nombre);
            itemView.setOnClickListener(this);
        }

        public void bind (Persona persona){
            mPersona = persona;
            mNombreTextView.setText(mPersona.getmNombre());
            mCelularTextView.setText(mPersona.getmCelular());
            mPersonaImageView.setImageResource(R.drawable.usuario);
        }

        @Override
        public void onClick(View v) {
            personaId = mPersona.getmIdPersona();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PERSONA_ID, personaId);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            Dialog dialog = getDialog();
            dialog.cancel();
        }
    }



}


