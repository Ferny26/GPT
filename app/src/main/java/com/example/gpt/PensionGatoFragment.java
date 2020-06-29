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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PensionGatoFragment extends Fragment {
    private NumberPicker mMesNumberPicker, mAñoNumberPicker;
    private EditText mNombreGatoEditText, mNombrePersonaEditText, mPesoEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText;
    private EditText  mCondicionEditText, mEmailEditText;
    private CheckBox mResponsableCheckBox, mCondicionEspecialCheckBox;
    private RadioGroup mSexoRadioGroup;
    private RadioButton mHembraRadioButton, mMachoRadioButton;
    private TextView procedencia;
    private Date mFechaNacimiento = new Date();
    private ConstraintLayout mFormularioResponsableConstraintLayout;
    private Button mBuscarResponsableButton, mBuscarGatoButton;
    private ImageButton mCameraImageButton;
    private ImageView mGatoImagenImageView;
    private String  mTitle;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final int REQUEST_FOTO = 1;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Bundle arguments = new Bundle();
    private UUID pensionId;
    private Gato mGato;
    private Persona mResponsable;
    CatLab mCatLab;
    private Pension mPension;
    RadioButton mRadioSexo;


    private EventBus bus = EventBus.getDefault();
    private File mPhotoFile;
    Uri photoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
        bus.register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gato_fragment, null);
        boolean nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");
        if(!nuevaInstancia) {
            pensionId = (UUID) getArguments().getSerializable("PENSION_ID");
        }
        //////////////////////////////////////////////// Wiring Up /////////////////////////////////////////////////
        mNombreGatoEditText = view.findViewById(R.id.nombreMaterial);
        mNombrePersonaEditText = view.findViewById(R.id.nombre_responsable);
        mApellidoPaternoEditText = view.findViewById(R.id.apellido_paterno_responsable);
        mApellidoMaternoEditText = view.findViewById(R.id.apellido_materno_responsable);
        mPesoEditText = view.findViewById(R.id.cantidad);
        mDomicilioEditText = view.findViewById(R.id.domicilio_responable);
        mCelularEditText = view.findViewById(R.id.celular_responsable);
        mCondicionEditText = view.findViewById(R.id.condicion);
        mResponsableCheckBox = view.findViewById(R.id.responsable);
        mCondicionEspecialCheckBox = view.findViewById(R.id.condicion_especial);
        mFormularioResponsableConstraintLayout = view.findViewById(R.id.formulario_responsable);
        mBuscarGatoButton = view.findViewById(R.id.buscar_gato);
        mBuscarResponsableButton = view.findViewById(R.id.buscar_responsable);
        mSexoRadioGroup = view.findViewById(R.id.sexo);
        mEmailEditText = view.findViewById(R.id.email_responsable);
        mMesNumberPicker = view.findViewById(R.id.mes_select);
        mAñoNumberPicker = view.findViewById(R.id.fecha_año_select);
        mCameraImageButton = view.findViewById(R.id.cameraButton);
        mGatoImagenImageView = view.findViewById(R.id.materialImagen);
        procedencia = view.findViewById(R.id.textView10);
        mHembraRadioButton = view.findViewById(R.id.hembra);
        mMachoRadioButton = view.findViewById(R.id.macho);
        mCatLab = CatLab.get(getActivity());
        mResponsableCheckBox.setChecked(true);
        mResponsableCheckBox.setVisibility(View.GONE);
        procedencia.setVisibility(View.GONE);
        mFormularioResponsableConstraintLayout.setVisibility(View.VISIBLE);
        if(mGato==null){
            mGato = new Gato();
            mGato.setmProcedencia(2);
        }
        if(mResponsable==null){
            mResponsable = new Persona();
        }
        if(mPension==null){
            mPension = new Pension();
        }

        if (pensionId != null){
            mPension = PensionStorage.get(getActivity()).getmPension(pensionId);
            mGato = mCatLab.getmGato(mPension.getmGatoId());
            GatoDefinido();
            mBuscarGatoButton.setEnabled(false);
            GatoHogarLab mgatoHogarLab = GatoHogarLab.get(getActivity());
            GatoHogar mgatoHogar = mgatoHogarLab.getmGatoHogar(mGato.getmIdGato());
            if (mgatoHogar != null){
                mBuscarResponsableButton.setEnabled(false);
                PersonaStorage mPersonaStorage = PersonaStorage.get(getActivity());
                mResponsable = mPersonaStorage.getmPersona(mgatoHogar.getmPersonaId());
                ResponsableDefinido();
            }
        }


        //////////////////////////////////////////////////////////////////////////////////////////////////// Spinner ///////////////////////////////////////////////////////////////////////////////////////////////////
        //Seleccion de procedencia


        ////////////////////////////////////////////////////////////////////////////////////////////////////////// Number Pickers /////////////////////////////////////////////////////////////////////////////////////////////////////

        mMesNumberPicker.setMinValue(1);
        mMesNumberPicker.setMaxValue(12);
        mAñoNumberPicker.setMinValue(2000);
        mAñoNumberPicker.setMaxValue(2020);

        mAñoNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int año = mAñoNumberPicker.getValue();
                mFechaNacimiento.setYear(año);
                mGato.setmFechaNacimiento(mFechaNacimiento);
            }
        });

        mMesNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int mes = mMesNumberPicker.getValue();
                mFechaNacimiento.setMonth(mes);
                mGato.setmFechaNacimiento(mFechaNacimiento);
            }
        });

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


        //Busquedas Gato y Persona
        mBuscarResponsableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = "Responsable";
                Busqueda();
            }
        });

        mBuscarGatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = "Gato";
                Busqueda();

            }
        });

        mResponsableCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mFormularioResponsableConstraintLayout.setVisibility(View.VISIBLE);
                    if(!mNombrePersonaEditText.getText().toString().isEmpty()){
                        mResponsable.setmNombre(mNombrePersonaEditText.getText().toString());
                    }
                    if(!mApellidoMaternoEditText.getText().toString().isEmpty()){
                        mResponsable.setmApellidoMaterno(mApellidoMaternoEditText.getText().toString());
                    }
                    if(!mApellidoPaternoEditText.getText().toString().isEmpty()){
                        mResponsable.setmApellidoPaterno(mApellidoPaternoEditText.getText().toString());
                    }
                    if(!mEmailEditText.getText().toString().isEmpty()){
                        mResponsable.setmEmail(mEmailEditText.getText().toString());
                    }
                    if(!mCelularEditText.toString().isEmpty()){
                        mResponsable.setmCelular(mCelularEditText.getText().toString());
                    }
                    if(!mDomicilioEditText.getText().toString().isEmpty()){
                        mResponsable.setmDomicilio(mDomicilioEditText.getText().toString());
                    }
                }
                else {
                    mFormularioResponsableConstraintLayout.setVisibility(View.GONE);
                }
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
        mNombrePersonaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mResponsable.setmNombre(s.toString());
                if(mResponsable.getmNombre().isEmpty()){
                    mResponsable.setmNombre(null);
                }
            }
        });
        mApellidoPaternoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mResponsable.setmApellidoPaterno(s.toString());
                if(mResponsable.getmApellidoPaterno().isEmpty()){
                    mResponsable.setmApellidoPaterno(null);
                }
            }
        });
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mResponsable.setmEmail(s.toString());
                if(mResponsable.getmEmail().isEmpty()){
                    mResponsable.setmEmail(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mApellidoMaternoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mResponsable.setmApellidoMaterno(s.toString());
                if(mResponsable.getmApellidoMaterno().isEmpty()){
                    mResponsable.setmApellidoMaterno(null);
                }
            }
        });
        mDomicilioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mResponsable.setmDomicilio(s.toString());
                if(mResponsable.getmDomicilio().isEmpty()){
                    mResponsable.setmDomicilio(null);
                }
            }

        });
        mCelularEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mResponsable.setmCelular(s.toString());
                if(mResponsable.getmCelular().isEmpty()){
                    mResponsable.setmCelular(null);
                }
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
        String query = "SELECT * FROM gatos WHERE NOT EXISTS (SELECT * FROM pensiones WHERE gatos.uuid = pensiones.gato_id)";
        arguments.putSerializable("TITLE", mTitle);
        arguments.putSerializable("QUERY",query);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(PensionGatoFragment.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }

    private void GatoDefinido(){
        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPesoEditText.setText(mGato.getmPeso());
        mMesNumberPicker.setValue(mGato.getmFechaNacimiento().getMonth());
        mAñoNumberPicker.setValue(mGato.getmFechaNacimiento().getYear());

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
    }

    private void ResponsableDefinido(){
        mNombrePersonaEditText.setText(mResponsable.getmNombre());
        mApellidoPaternoEditText.setText(mResponsable.getmApellidoPaterno());
        mApellidoMaternoEditText.setText(mResponsable.getmApellidoMaterno());
        mEmailEditText.setText(mResponsable.getmEmail());
        mDomicilioEditText.setText(mResponsable.getmDomicilio());
        mCelularEditText.setText(mResponsable.getmCelular());
    }

    @Subscribe
    public void recibirGato(Gato gato){
        mGato = gato;
    }

    @Subscribe
    public void recibirPension(Pension pension){
        mPension = pension;
    }

    @Subscribe
    public void recibirResponsable(Persona responsable){
        mResponsable = responsable;
        ResponsableDefinido();
    }
    @Override
    public void onPause() {
        bus.post(mPension);
        mGato.setValidacion(verificacion());
        bus.post(mGato);
        bus.post(mResponsable);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    private boolean verificacion(){
        boolean validacionDatos = true;

        if((mResponsable.getmNombre() == null || mResponsable.getmCelular() == null)){
            validacionDatos = false;
        }
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
            if(mTitle == "Gato"){
                UUID gatoId = (UUID) data.getSerializableExtra(Busqueda.EXTRA_GATO_ID);
                mGato = mCatLab.getmGato(gatoId);
                GatoDefinido();
                mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
                photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
                putImageView();
                GatoHogarLab mgatoHogarLab = GatoHogarLab.get(getActivity());
                GatoHogar mgatoHogar = mgatoHogarLab.getmGatoHogar(mGato.getmIdGato());
                if (mgatoHogar != null){
                    PersonaStorage mPersonaStorage = PersonaStorage.get(getActivity());
                    mResponsable = mPersonaStorage.getmPersona(mgatoHogar.getmPersonaId());
                    ResponsableDefinido();
                }
            }
            else{
                UUID personaId = (UUID) data.getSerializableExtra(Busqueda.EXTRA_PERSONA_ID);
                mResponsable = PersonaStorage.get(getActivity()).getmPersona(personaId);
                ResponsableDefinido();
            }

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
