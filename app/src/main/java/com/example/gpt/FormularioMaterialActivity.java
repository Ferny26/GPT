package com.example.gpt;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

//Clase para poder ingresar el formulario del material
public class FormularioMaterialActivity extends AppCompatActivity {
    private EditText mNombreEditText, mPresentacionEditText, mCantidadEditText;
    private Spinner mCategoriaSpinner;
    private Button mGuardarButton;
    private Material mMaterial;
    private ImageView mMaterialImageView;
    private static final int REQUEST_FOTO = 1;
    private String [] mCategoriaList = {"Quirurgico", "Medicamento", "Poducto Desinfectante", "Otros"};
    private File  mPhotoFile;
    private  UUID materialId;
    private Uri mPhotoUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_fragment);
        mNombreEditText = findViewById(R.id.nombreMaterial);
        mPresentacionEditText = findViewById(R.id.Presentacion);
        mCantidadEditText = findViewById(R.id.cantidad);
        mCategoriaSpinner = findViewById(R.id.categoria);
        ImageButton mCameraButton = findViewById(R.id.cameraButton);
        mGuardarButton = findViewById(R.id.Guardar);
        mMaterialImageView = findViewById(R.id.materialImagen);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCategoriaList);
        mCategoriaSpinner.setAdapter(mAdapter);
        materialId = (UUID) getIntent().getSerializableExtra("MATERIAL_ID");
        Boolean type = getIntent().getBooleanExtra("TYPE",false);
        //Si es un material ya creado coloca los datos, si no, se crea uni nuevo
        if (materialId==null){
            mMaterial = new Material();
        }else {
            mMaterial = MaterialStorage.get(getApplicationContext()).getmMaterial(materialId);
            ColocarMaterial();
        }
        if (type){
            mMaterial.setmTipoInventario(1);
        }else {
            mMaterial.setmTipoInventario(2);
        }

        mCategoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMaterial.setmCategoria(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mNombreEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               mMaterial.setmNombre(mNombreEditText.getText().toString());
                if(mMaterial.getmNombre().isEmpty()){
                    mMaterial.setmNombre(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPresentacionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMaterial.setmPresentacion(mPresentacionEditText.getText().toString());
                if(mMaterial.getmPhotoFile().isEmpty()){
                    mMaterial.setmPresentacion(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCantidadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    mMaterial.setmCantidad(Integer.parseInt(mCantidadEditText.getText().toString()));

                } catch (Exception e) {
                    mMaterial.setmCantidad(0);
                    e.printStackTrace();
                }
            }
        });

        PackageManager packageManager = this.getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoFile = MaterialStorage.get(this).getPhotoFile(mMaterial);
        mPhotoUri = FileProvider.getUriForFile(this, "com.example.gpt.FileProvider", mPhotoFile);

        putImageView();

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtiene el uri de la imagen con su ruta especifica para poder guardar el archivo una vez que la foto se haya tomado
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                List<ResolveInfo> cameraActivities = getApplication().
                        getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                //Se otorga el permiso siempre y cuando la actividad tenga permitido usarla y esta no este usada por otras actividades
                for(ResolveInfo activity : cameraActivities){
                    getApplication().grantUriPermission("activity.activityInfo", mPhotoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //Se llama a la camara para poder obtener su resultado
                startActivityForResult(captureImage, REQUEST_FOTO);
            }
        });

        mGuardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validacion = validacion();
                if (validacion){
                    if(materialId!=null){
                        MaterialStorage.get(getApplication()).updateMaterial(mMaterial);
                    }else {
                        MaterialStorage.get(getApplication()).addMaterial(mMaterial, getApplication());
                    }
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Datos incompletos",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_FOTO ){
            putImageView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ColocarMaterial(){
        mGuardarButton.setText("Actualizar");
        mNombreEditText.setText(mMaterial.getmNombre());
        String mCantidad = Integer.toString(mMaterial.getmCantidad());
        mCantidadEditText.setText(mCantidad);
        mPresentacionEditText.setText(mMaterial.getmPresentacion());
        mCategoriaSpinner.setSelection(mMaterial.getmCategoria());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.borrar_material, menu);
        MenuItem delete = menu.findItem(R.id.borrar_material);
        if (materialId == null){
            delete.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borrar_material:
                final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(this);
                mDeleteDialog.setTitle("Borrar Material")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Estas segura de borrar el material")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MaterialStorage.get(getApplication()).deleteMaterial(GPTDbSchema.MaterialTable.Cols.UUID + "= ?", new String[]{mMaterial.getmMaterialId().toString()});
                                        finish();
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
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validacion (){
        if(mMaterial.getmCantidad()==0 || mMaterial.getmNombre() == null || mMaterial.getmPresentacion()==null){
            return false;
        }
        return true;
    }

    private void putImageView() {
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoUri);
            //Asignacion de orientacion correcta para la foto
            ExifInterface exif = null;
            exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            //obtiene la orientacion de la foto
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //Envia la orientacion y el bitmap como parametros para modificarlos
            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
            //Una vez adecuada la foto, se coloca en el imageView
            mMaterialImageView.setImageBitmap(bmRotated);
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

}
