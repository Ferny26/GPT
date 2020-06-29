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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MaterialCampañaFragment extends Fragment {
    private ImageView mMainImageView;
    private MaterialCampañaStorage mMaterialStorage;
    private TextView mCantidadGastadaTextView;
    private MaterialCampañaFragment.MaterialAdapter mAdapter;
    private RecyclerView mMaterialesRecyclerView;
    private UUID campañaId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.material_de_campaña));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_list_activity, null);
        campañaId = (UUID) getArguments().getSerializable("ARG_CAMPAÑA_ID");
        mMainImageView = view.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.medicina_color);
        mMaterialesRecyclerView = view.findViewById(R.id.recyclerView);
        mMaterialesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    private void updateUI (){
        mMaterialStorage = MaterialCampañaStorage.get(getActivity());
        List<MaterialCampaña> materiales;
        String query = "SELECT * FROM material_campaña WHERE campaña_id = '"+ campañaId.toString() +"'";
        materiales = mMaterialStorage.getmBusquedaMaterialesCampaña(query);


        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new MaterialCampañaFragment.MaterialAdapter(materiales);
            mMaterialesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmMateriales(materiales);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mMaterialesRecyclerView.setAdapter(mAdapter);
        }

    }



    private class MaterialesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView;
        private ImageView mMaterialImageView;
        private MaterialCampaña mMaterialCampaña;

        @Override
        public void onClick(View v) {
            final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(getActivity());
            mDeleteDialog.setTitle("Borrar material gastado")
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .setMessage("Estas segura de borrar este material de campaña, la cantidad gastada no se recupera")
                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Elimina el crimen de la BD y regresa a la pantalla anterior

                                    String query = "DELETE FROM material_campaña WHERE material_campaña.campaña_id = '"+
                                            campañaId.toString() + "' AND material_campaña.material_id = '" + mMaterialCampaña.getmMaterialId().toString() + "'";
                                    MaterialCampañaStorage.get(getActivity()).deleteMaterial(query);
                                    updateUI();
                                    Material material = MaterialStorage.get(getContext()).getmMaterial(mMaterialCampaña.getmMaterialId());
                                    int cantidadSumada = mMaterialCampaña.getmCantidadGastada();
                                    material.setmCantidad(material.getmCantidad() + cantidadSumada);
                                    MaterialStorage.get(getContext()).updateMaterial(material);
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
        }

        public MaterialesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.material_list, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mCantidadGastadaTextView = itemView.findViewById(R.id.cantidad_gastada);
            mCantidadGastadaTextView.setVisibility(View.VISIBLE);
            mMaterialImageView= itemView.findViewById(R.id.material_foto);
            itemView.setOnClickListener(this);
        }

        public void bind (MaterialCampaña materialcampaña){
            mMaterialCampaña = materialcampaña;
            Material material = MaterialStorage.get(getActivity()).getmMaterial(mMaterialCampaña.getmMaterialId());
            mNombreTextView.setText(material.getmNombre());
            mCantidadGastadaTextView.setText(Integer.toString(mMaterialCampaña.getmCantidadGastada()));
            putImageView(material, mMaterialImageView);
        }
    }




    private class MaterialAdapter extends RecyclerView.Adapter<MaterialCampañaFragment.MaterialesHolder>{
        private List<MaterialCampaña> mMateriales;
        public MaterialAdapter (List<MaterialCampaña> materiales){
            mMateriales = materiales;
        }

        @NonNull
        @Override
        public MaterialCampañaFragment.MaterialesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MaterialCampañaFragment.MaterialesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MaterialCampañaFragment.MaterialesHolder holder, int position) {
            MaterialCampaña material = mMateriales.get(position);
            holder.bind(material);
        }
        @Override
        public int getItemCount() {
            return mMateriales.size();
        }

        public void setmMateriales(List<MaterialCampaña> materiales ){
            mMateriales = materiales;
        }
    }

    private void putImageView(Material mMaterial, ImageView mMaterialImageView) {
        File mPhotoFile = MaterialStorage.get(getActivity()).getPhotoFile(mMaterial);
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
                mMaterialImageView.setImageBitmap(bmRotated);
            }
        } catch (IOException e) {
            mMaterialImageView.setImageResource(R.drawable.medicina_color);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.agregar_material, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.agregar_material:
                Intent intent = new Intent(getContext(), AgregarMaterialActivity.class);
                intent.putExtra("CAMPAÑA_ID", campañaId);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
