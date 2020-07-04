package com.example.gpt;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


//Fragmento que contiene la lista de todas las esterilizaciones de la campaña seleccionada
public class EsterilizacionesFragment extends Fragment {
    private UUID campañaId;
    private EsterilizacionStorage mEsterilizacionStorage;
    private EsterilizacionAdapter mAdapter;
    private RecyclerView mEsterilizacionesRecyclerView;
    private ImageView mGatoFotoImageView;
    private static final int REQUEST_CREATE = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private boolean mSubtitleVisible;
    private Random mRandom=new Random();
    private ArrayList <Integer> imageViewArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("Esterilizaciones");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.universal_list_activity, null);
        campañaId = (UUID) getArguments().getSerializable("ARG_CAMPAÑA_ID");
        mEsterilizacionesRecyclerView = v.findViewById(R.id.recyclerView);
        ImageView mMainImageView = v.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.gato_esterilizacion_color);
        mEsterilizacionesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button mEstadisticasButton = v.findViewById(R.id.estadisticas);
        updateUI();
        imageViewArrayList = new ArrayList<Integer>();
        imageViewArrayList.add(R.drawable.gato_blanco);
        imageViewArrayList.add(R.drawable.gato_blanco_2);
        imageViewArrayList.add(R.drawable.gato_cremas);
        imageViewArrayList.add(R.drawable.gato_gris_2);
        imageViewArrayList.add(R.drawable.gato_gris_3);

        mEstadisticasButton.setVisibility(View.VISIBLE);


        mEstadisticasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                FragmentManager manager = getFragmentManager();
                EstadisticasDialog dialog = new EstadisticasDialog();
                arguments.putSerializable("CAMPAÑA_ID", campañaId);
                dialog.setArguments(arguments);
                dialog.show(manager,DIALOG_CREATE);
            }
        });
        return v;
    }

    private void updateUI (){
        mEsterilizacionStorage = EsterilizacionStorage.get(getActivity());
        List<Esterilizacion> esterilizaciones = mEsterilizacionStorage.getmEsterilizaciones(campañaId);
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new EsterilizacionAdapter(esterilizaciones);
            mEsterilizacionesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmEsterilizaciones(esterilizaciones);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mEsterilizacionesRecyclerView.setAdapter(mAdapter);
        }

        updateSubtitle();

    }


    private class EsterilizacionesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView;
        private ImageView  mPagoImageView;
        private Esterilizacion mEsterilizacion;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), EsterilizacionActivity.class);
            intent.putExtra("ESTERILIZACION_ID", mEsterilizacion.getmIdEsterilizacion());
            startActivity(intent);
        }

        public EsterilizacionesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.esterilizacion_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mPagoImageView= itemView.findViewById(R.id.esterilizacion_pagada);
            mGatoFotoImageView= itemView.findViewById(R.id.material_foto);
            itemView.setOnClickListener(this);
        }

        public void bind (Esterilizacion esterilizacion, Gato mgato){
            mEsterilizacion= esterilizacion;
            if(mEsterilizacion.ismPagado()){
                mPagoImageView.setVisibility(View.VISIBLE);
            }else{
                mPagoImageView.setVisibility(View.INVISIBLE);
            }
            mNombreTextView.setText(mgato.getmNombreGato());

            putImageView(mgato);
        }
    }




    private class EsterilizacionAdapter extends RecyclerView.Adapter<EsterilizacionesFragment.EsterilizacionesHolder>{
        private List<Esterilizacion> mEsterilizaciones;
        public EsterilizacionAdapter (List<Esterilizacion> esterilizaciones){
            mEsterilizaciones = esterilizaciones;
        }

        @NonNull
        @Override
        public EsterilizacionesFragment.EsterilizacionesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EsterilizacionesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EsterilizacionesFragment.EsterilizacionesHolder holder, int position) {
            Esterilizacion esterilizacion = mEsterilizaciones.get(position);
            CatLab mCatLab = CatLab.get(getActivity());
            Gato mgato = mCatLab.getmGato(esterilizacion.getmIdGato());
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

    private void putImageView(Gato mGato) {
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
                mGatoFotoImageView.setImageBitmap(bmRotated);
            }
        } catch (IOException e) {
            mGatoFotoImageView.setImageResource(imageViewArrayList.get(mRandom.nextInt(5)));
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

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.añadir_esterilizacion:
                Intent intent = new Intent(getActivity(), EsterilizacionActivity.class);
                intent.putExtra("CAMPAÑA_ID",campañaId);
                startActivity(intent);
                return true;
            case R.id.editar_campaña:
                Bundle arguments = new Bundle();
                FragmentManager manager = getFragmentManager();
                CrearCampañaDialog dialog = new CrearCampañaDialog();
                arguments.putSerializable("CAMPAÑA_ID", campañaId);
                arguments.putBoolean("NUEVA_INSTANCIA", true);
                dialog.setArguments(arguments);
                dialog.setTargetFragment(EsterilizacionesFragment.this, REQUEST_CREATE);
                dialog.show(manager,DIALOG_CREATE);
                return true;
            case R.id.show_esterilizaciones:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            case R.id.borrar_campaña:
                final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(getActivity());
                mDeleteDialog.setTitle("Borrar campaña")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Estas segura de borrar la campaña, se borraras todas las esterilizaciones")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Elimina el crimen de la BD y regresa a la pantalla anterior
                                        CampañaStorage.get(getActivity()).deleteCampaña(GPTDbSchema.CampañaTable.Cols.UUID + "= ?", new String[] {campañaId.toString()});
                                        getActivity().finish();
                                    }
                                })
                        //Cancela la accion de delete
                        .setNegativeButton("cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .create().show();
                return true;

            case R.id.inventario:
                Intent intent2 = new Intent(getActivity(), MaterialCampañaActivity.class);
                intent2.putExtra("CAMPAÑA_ID",campañaId);
                startActivity(intent2);
                return true;
                
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void updateSubtitle(){
        int esterilizacionesCount = mEsterilizacionStorage.getmEsterilizaciones(campañaId).size();
        String subtitle = getString(R.string.show_esterilizaciones, esterilizacionesCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.esterilizaciones_menu,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_esterilizaciones);
        //Cambia el subtitulo según la opción seleccionada
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.ocultar);
        }else {
            subtitleItem.setTitle(R.string.contar_esterilziacioens);
        }
    }
}
