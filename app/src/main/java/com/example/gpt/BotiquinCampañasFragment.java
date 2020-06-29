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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BotiquinCampañasFragment extends Fragment {
    private ImageView mMainImageView;
    private BotiquinCampañasFragment.MaterialAdapter mAdapter;
    private RecyclerView mMaterialesRecyclerView;
    private Boolean type;
    List<Material> materiales;
    private String query;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.inventario_botiquin));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_list_activity, null);
        mMainImageView = view.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.medicina_color);
        mMaterialesRecyclerView = view.findViewById(R.id.recyclerView);
        mMaterialesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        type = getArguments().getBoolean("TYPE",false);
        updateUI();
        return view;
    }
    private void updateUI (){
        if (type){
            query = "SELECT * FROM materiales WHERE materiales.tipo_inventario=1";
        }else{
            query ="SELECT * FROM materiales WHERE materiales.tipo_inventario=2";
        }
        materiales = MaterialStorage.get(getActivity()).getmBusquedaMateriales(query);
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new MaterialAdapter(materiales);
            mMaterialesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmMateriales(materiales);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mMaterialesRecyclerView.setAdapter(mAdapter);
        }

    }


    private class MaterialesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView, mCantidadTextView;
        private ImageView mMaterialImageView;
        private Material mMaterial;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), FormularioMaterialActivity.class);
            intent.putExtra("MATERIAL_ID", mMaterial.getmMaterialId());
            startActivity(intent);
        }

        public MaterialesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.material_list, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mMaterialImageView= itemView.findViewById(R.id.material_foto);
            mCantidadTextView = itemView.findViewById(R.id.cantidad_gastada);
            itemView.setOnClickListener(this);
        }

        public void bind (Material material){
            mMaterial=material;
            mNombreTextView.setText(mMaterial.getmNombre());
            putImageView(mMaterial, mMaterialImageView);
            mCantidadTextView.setVisibility(View.VISIBLE);
            mCantidadTextView.setText(Integer.toString(mMaterial.getmCantidad()));
        }
    }




    private class MaterialAdapter extends RecyclerView.Adapter<BotiquinCampañasFragment.MaterialesHolder>{
        private List<Material> mMateriales;
        public MaterialAdapter (List<Material> materiales){
            mMateriales = materiales;
        }

        @NonNull
        @Override
        public BotiquinCampañasFragment.MaterialesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BotiquinCampañasFragment.MaterialesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BotiquinCampañasFragment.MaterialesHolder holder, int position) {
            Material material = mMateriales.get(position);
            holder.bind(material);
        }
        @Override
        public int getItemCount() {
            return mMateriales.size();
        }

        public void setmMateriales(List<Material> materiales ){
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
        inflater.inflate(R.menu.botiquin_campanias, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.inventario_botiquin:
                Intent intent = new Intent(getContext(), FormularioMaterialActivity.class);
                intent.putExtra("TYPE",type);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
