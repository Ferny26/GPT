package com.example.gpt;

import android.Manifest;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class GatoFragmentAdopciones extends Fragment {

    private Spinner mProcedenciaSpinner;
    private NumberPicker mMesNumberPicker, mAñoNumberPicker;
    private EditText mNombreGatoEditText, mPesoEditText, mCondicionEditText;
    private CheckBox  mCondicionEspecialCheckBox;
    private RadioGroup mSexoRadioGroup;
    private RadioButton mHembraRadioButton, mMachoRadioButton;
    private String  mTitle;
    private Date mFechaNacimiento = new Date();
    private Button mBuscarGatoButton, mGuardarButton;
    private ImageButton mCameraImageButton;
    private ImageView mGatoImagenImageView;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final int REQUEST_FOTO = 1;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Bundle arguments = new Bundle();
    private CatLab mCatLab;
    RadioButton mRadioSexo;
    private String [] mProcedenciaList = {"Recien rescatado", "Feral", "Propio"};
    private Gato mGato;
    private File mPhotoFile;
    private UUID gatoId;
    Uri photoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gato_fragment, null);

        //////////////////////////////////////////////// Wiring Up /////////////////////////////////////////////////
        mProcedenciaSpinner = view.findViewById(R.id.categoria);
        mNombreGatoEditText = view.findViewById(R.id.nombreMaterial);
        mPesoEditText = view.findViewById(R.id.cantidad);
        mCondicionEditText = view.findViewById(R.id.condicion);
        mCondicionEspecialCheckBox = view.findViewById(R.id.condicion_especial);
        mBuscarGatoButton = view.findViewById(R.id.buscar_gato);
        mSexoRadioGroup = view.findViewById(R.id.sexo);
        mMesNumberPicker = view.findViewById(R.id.mes_select);
        mAñoNumberPicker = view.findViewById(R.id.fecha_año_select);
        mCameraImageButton = view.findViewById(R.id.cameraButton);
        mGatoImagenImageView = view.findViewById(R.id.materialImagen);
        mHembraRadioButton = view.findViewById(R.id.hembra);
        mMachoRadioButton = view.findViewById(R.id.macho);
        mGuardarButton = view.findViewById(R.id.guardar);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mProcedenciaList);
        mProcedenciaSpinner.setAdapter(mAdapter);
        mCatLab = CatLab.get(getActivity());

        Boolean gato = getArguments().getBoolean("GATO");
        if (gato){
            gatoId= (UUID) getArguments().getSerializable("GATO_ID");
            mGato = CatLab.get(getActivity()).getmGato(gatoId);
            GatoDefinido();
        }else{
            mGato = new Gato();
        }

        mGuardarButton.setVisibility(View.VISIBLE);



        //////////////////////////////////////////////////////////////////////////////////////////////////// Spinner ///////////////////////////////////////////////////////////////////////////////////////////////////
        //Seleccion de procedencia
        mProcedenciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGato.setmProcedencia(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////// Number Pickers /////////////////////////////////////////////////////////////////////////////////////////////////////

        mMesNumberPicker.setMinValue(1);
        mMesNumberPicker.setMaxValue(12);
        mAñoNumberPicker.setMinValue(2000);
        mAñoNumberPicker.setMaxValue(2020);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////// Buttons y Check Boxes /////////////////////////////////////////////////////////////////////////////

        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        int radioId = mSexoRadioGroup.getCheckedRadioButtonId();
        mRadioSexo = mSexoRadioGroup.findViewById(radioId);
        mGato.setmSexo(mRadioSexo.getText().toString());

        putImageView();
        mCameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtiene el uri de la imagen con su ruta especifica para poder guardar el archivo una vez que la foto se haya tomado
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                List<ResolveInfo> cameraActivities = getActivity().
                        getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                //Se otorga el permiso siempre y cuando la actividad tenga permitido usarla y esta no este usada por otras actividades
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission("activity.activityInfo", photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //Se llama a la camara para poder obtener su resultado
                startActivityForResult(captureImage, REQUEST_FOTO);
            }
        });


        mBuscarGatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = "Gato";
                Busqueda();

            }
        });


        mCondicionEspecialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCondicionEditText.setVisibility(View.VISIBLE);
                }
                else {
                    mGato.setmCondicionEspecial(null);
                    mCondicionEditText.setVisibility(View.GONE);
                }
            }
        });

        mSexoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mRadioSexo = view.findViewById(checkedId);
                mGato.setmSexo(mRadioSexo.getText().toString());

            }
        });

        mGuardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificacion()){
                    if(mCatLab.getmGato(mGato.getmIdGato())==null){
                        mCatLab.addGato(mGato, getActivity());
                        Adopcion mAdopcion = new Adopcion(mGato.getmIdGato());
                        AdopcionStorage.get(getActivity()).addAdopcion(mAdopcion, getActivity());
                    }else if(AdopcionStorage.get(getActivity()).getmAdopcion(mGato.getmIdGato())==null){
                        mCatLab.updateGato(mGato);
                        Adopcion mAdopcion = new Adopcion(mGato.getmIdGato());
                        AdopcionStorage.get(getActivity()).addAdopcion(mAdopcion, getActivity());
                    }else {
                        mCatLab.updateGato(mGato);
                    }
                }else {
                    Toast.makeText(getActivity(), "Datos incompletos",
                            Toast.LENGTH_LONG).show();
                }
                getActivity().finish();
            }
        });

        //////////////////////////////////////////////// Edit Texts /////////////////////////////////////////////////

        mCondicionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGato.setmCondicionEspecial(s.toString());
                if(mGato.getmCondicionEspecial().isEmpty()){
                    mGato.setmCondicionEspecial(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mNombreGatoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGato.setmNombreGato(mNombreGatoEditText.getText().toString());
                if(mGato.getmNombreGato().isEmpty()){
                    mGato.setmNombreGato(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPesoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mGato.setmPeso(mPesoEditText.getText().toString());
                if(mGato.getmPeso().isEmpty()){
                    mGato.setmPeso(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }


    //////////////////////////////////////////////// Functions /////////////////////////////////////////////

    private void Busqueda (){
        FragmentManager manager = getFragmentManager();
        Busqueda dialog = new Busqueda();
        String query =  "SELECT * FROM gatos WHERE NOT EXISTS (SELECT * FROM adopciones WHERE gatos.uuid = adopciones.gato_id)";
        arguments.putSerializable("TITLE", mTitle);
        arguments.putString("QUERY",query);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(GatoFragmentAdopciones.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }

    private void GatoDefinido(){
        mGuardarButton.setText("Actualizar");
        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPesoEditText.setText(mGato.getmPeso());


        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPesoEditText.setText(mGato.getmPeso());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mGato.getmFechaNacimiento());

        int mes = calendar.get(Calendar.MONTH);
        int año = calendar.get(Calendar.YEAR);

        mMesNumberPicker.setValue(mes + 1);
        mAñoNumberPicker.setValue(año);

        if(mGato.ismSexo().equals("Hembra")){
            mHembraRadioButton.setChecked(true);
        }
        else if(mGato.ismSexo().equals("Macho")){
            mMachoRadioButton.setChecked(true);
        }

        if(mGato.getmCondicionEspecial() !=null){
            mCondicionEspecialCheckBox.setChecked(true);
            mCondicionEditText.setVisibility(View.VISIBLE);
            mCondicionEditText.setText(mGato.getmCondicionEspecial());
        }
        mProcedenciaSpinner.setSelection(mGato.getmProcedencia());
    }

    private boolean verificacion(){
        boolean validacionDatos = true;

        int mes = mMesNumberPicker.getValue();
        int año = mAñoNumberPicker.getValue();
        Date fecha = new GregorianCalendar(año, mes-1, 5).getTime();
        mGato.setmFechaNacimiento(fecha);

        if(mCondicionEspecialCheckBox.isChecked() && (mGato.getmCondicionEspecial() == null)){
            mGato.setmCondicionEspecial(null);
            validacionDatos = false;
        }
        if(mGato.getmNombreGato() == null || mGato.getmPeso() == null  || mFechaNacimiento == null){
            validacionDatos = false;
        }
        return validacionDatos;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_BUSQUEDA){
                UUID gatoId = (UUID) data.getSerializableExtra(Busqueda.EXTRA_GATO_ID);
                mGato = mCatLab.getmGato(gatoId);
                GatoDefinido();
                mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
                photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
                putImageView();
        }else if(requestCode == REQUEST_FOTO ){
            putImageView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void putImageView() {
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
            mGatoImagenImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
            mGatoImagenImageView.setImageResource(R.drawable.gato_gris);
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

