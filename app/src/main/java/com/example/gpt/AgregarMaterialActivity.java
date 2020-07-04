package com.example.gpt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;



//Clase para agregar material
public class AgregarMaterialActivity extends AppCompatActivity {

    private AgregarMaterialActivity.MaterialAdapter mAdapter;
    private RecyclerView mMaterialesRecyclerView;
    private UUID campañaId;
    private EditText mCantidadGastadaEditText;

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        campañaId = (UUID) getIntent().getSerializableExtra("CAMPAÑA_ID");
        this.setTitle(getString(R.string.agregar_material));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_list_activity);
        ImageView mMainImageView = findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.medicina_color);
        mMaterialesRecyclerView = findViewById(R.id.recyclerView);
        mMaterialesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();

    }


    //Simplemente se impletenta un dialog para poder ingresar la cantidad del material
    public void showAlertDialogButtonClicked(final Material material) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agrega la cantidad gastada de material");
        final View customview = getLayoutInflater().inflate(R.layout.material_gastado_dialog, null);
        builder.setView(customview);
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCantidadGastadaEditText = customview.findViewById(R.id.material_gastado);
                sendDialogDataToActivity(mCantidadGastadaEditText.getText().toString(), material);
            }
        }

        );
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    //Cuando ya fuer agregado el matierial, se hace un intent para regresar a la actividad anterior y finalizando dicha actividad y quitandola de la cola para corresponder el lineamiento en orden
    private void sendDialogDataToActivity(String string, Material material) {
        if(string != null && !string.equals("") && Integer.parseInt(string) <= material.getmCantidad() && Integer.parseInt(string) != 0){
            int cantidad = Integer.parseInt(string);
            material.setmCantidad(material.getmCantidad()- cantidad);
            MaterialStorage.get(this).updateMaterial(material);
            MaterialCampaña mMaterialCampaña = new MaterialCampaña(campañaId);
            mMaterialCampaña.setmCantidadGastada(Integer.parseInt(string));
            mMaterialCampaña.setmMaterialId(material.getmMaterialId());
            MaterialCampañaStorage.get(getApplicationContext()).addMaterial(mMaterialCampaña, getApplicationContext());

            Intent intent = new Intent(this, MaterialCampañaActivity.class);
            intent.putExtra("CAMPAÑA_ID", campañaId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Datos invalidos o más material del que existe",
                    Toast.LENGTH_LONG).show();
        }

    }


    private void updateUI (){
        //Se trae el matierial que no está dentro de la actividad de material de campaña o en su correspondiente que no se encuentre en la tabla de material de campaña de la campaña seleccionada
        MaterialStorage mMaterialStorage = MaterialStorage.get(this);
        String query = "SELECT * FROM material WHERE NOT EXISTS (SELECT * FROM material_campaña WHERE material.uuid = material_campaña.material_id AND material_campaña.campaña_id = '"+ campañaId.toString()+"')";

        List<Material> materiales = mMaterialStorage.getmBusquedaMateriales(query);
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new AgregarMaterialActivity.MaterialAdapter(materiales);
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
        private TextView mCantidadTextView;
        private Material mMaterial;
        @Override
        public void onClick(View v) {
            showAlertDialogButtonClicked(mMaterial);
        }

        public MaterialesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.material_list, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mMaterialImageView= itemView.findViewById(R.id.material_foto);
            mCantidadTextView = itemView.findViewById(R.id.cantidad_gastada);
            mCantidadTextView.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        public void bind (Material material){
            mMaterial=material;
            mNombreTextView.setText(mMaterial.getmNombre());
            putImageView(mMaterial, mMaterialImageView);
            mCantidadTextView.setText(Integer.toString(mMaterial.getmCantidad()));

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MaterialCampañaActivity.class);
        intent.putExtra("CAMPAÑA_ID", campañaId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private class MaterialAdapter extends RecyclerView.Adapter<AgregarMaterialActivity.MaterialesHolder>{
        private List<Material> mMateriales;
        private MaterialAdapter(List<Material> materiales){
            mMateriales = materiales;
        }

        @NonNull
        @Override
        public AgregarMaterialActivity.MaterialesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new AgregarMaterialActivity.MaterialesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AgregarMaterialActivity.MaterialesHolder holder, int position) {
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
        File mPhotoFile = MaterialStorage.get(this).getPhotoFile(mMaterial);
        Uri photoUri = FileProvider.getUriForFile(this, "com.example.gpt.FileProvider", mPhotoFile);
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
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
}
