package com.example.gpt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdopcionFragment extends Fragment {
    private ImageView mMainImageView;
    private AdopcionAdapter mAdapter;
    private Spinner mEstatusAdopcionSpinner;
    private String [] mEstatusList = {"Adoptados", "Disponibles"};
    private RecyclerView mAdopcionesRecyclerView;
    private List<Adopcion> adopciones;
    private String query;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.universal_list_activity,container,false);
        mMainImageView= v.findViewById(R.id.main_image_view);
        mEstatusAdopcionSpinner = v.findViewById(R.id.estatus_adopcion);
        getActivity().setTitle("Adopciones");
        mMainImageView.setImageResource(R.drawable.adopcion);
        mAdopcionesRecyclerView = v.findViewById(R.id.recyclerView);
        setHasOptionsMenu(true);
        mEstatusAdopcionSpinner.setVisibility(View.VISIBLE);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mEstatusList);
        mEstatusAdopcionSpinner.setAdapter(mAdapter);
        mAdopcionesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mEstatusAdopcionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    query = "SELECT * FROM adopciones INNER JOIN registro_adopciones ON adopciones.gato_id = registro_adopciones.gato_id";
                    adopciones = AdopcionStorage.get(getActivity()).getmAdopciones(query);
                }else{
                    query ="SELECT * FROM adopciones WHERE NOT EXISTS (SELECT * FROM registro_adopciones WHERE adopciones.gato_id = registro_adopciones.gato_id)";
                    adopciones = AdopcionStorage.get(getActivity()).getmAdopciones(query);
                }
                updateUI();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        if (mEstatusAdopcionSpinner.getSelectedItemId()==1){
            query ="SELECT * FROM adopciones WHERE NOT EXISTS (SELECT * FROM registro_adopciones WHERE adopciones.gato_id = registro_adopciones.gato_id)";
        }else{
            query = "SELECT * FROM adopciones INNER JOIN registro_adopciones ON adopciones.gato_id = registro_adopciones.gato_id";
        }
        adopciones = AdopcionStorage.get(getActivity()).getmAdopciones(query);
        updateUI();
        super.onResume();
    }

    private void updateUI (){
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new AdopcionAdapter(adopciones);
            mAdopcionesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmAdopciones(adopciones);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mAdopcionesRecyclerView.setAdapter(mAdapter);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.adopcion_menu,menu);
    }

    private class AdopcionesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView;
        private ImageView mGatoImageView, mPagoImageView;
        private Adopcion mAdopcion;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AdopcionesActivity.class);
            intent.putExtra("TYPE", false);
            intent.putExtra("ADOPCION_ID", mAdopcion.getmAdopcionId());
            startActivity(intent);
        }

        public AdopcionesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.esterilizacion_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mGatoImageView= itemView.findViewById(R.id.material_foto);
            mPagoImageView = itemView.findViewById(R.id.esterilizacion_pagada);
            mPagoImageView.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        public void bind (Gato gato, Adopcion adopcion){
            mAdopcion = adopcion;
            mNombreTextView.setText(gato.getmNombreGato());
            putImageView(gato, mGatoImageView);
        }
    }


    private class AdopcionAdapter extends RecyclerView.Adapter<AdopcionFragment.AdopcionesHolder>{
        private List<Adopcion> mAdopciones;
        public AdopcionAdapter (List<Adopcion> adopciones){
            mAdopciones = adopciones;
        }

        @NonNull
        @Override
        public AdopcionFragment.AdopcionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AdopcionesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AdopcionFragment.AdopcionesHolder holder, int position) {
            Adopcion adopcion = mAdopciones.get(position);
            CatLab mCatLab = CatLab.get(getActivity());
            Gato mgato = mCatLab.getmGato(adopcion.getmAdopcionId());
            holder.bind(mgato, adopcion);
        }
        @Override
        public int getItemCount() {
            return mAdopciones.size();
        }

        public void setmAdopciones(List<Adopcion> adopciones ){
            mAdopciones = adopciones;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.añadir_adopcion:
                Intent intent = new Intent(getContext(), AdopcionesActivity.class);
                intent.putExtra("TYPE",true);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void putImageView(Gato mGato, ImageView mGatoImageView ) {
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
            mGatoImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
            mGatoImageView.setImageResource(R.drawable.gato_gris);
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
}
