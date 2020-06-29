package com.example.gpt;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class VistaAdopcionFragment extends Fragment {

    private EditText mNombrePersonaEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText, mEmailEditText;
    private ConstraintLayout mVistaAdopcion;
    private ScrollView mFormularioAdoptanteScrollView;
    private TextView mNombreGatoEditText, mAgregaAdoptanteTextView;
    private Button mDocumentoButton, mGuardarButton;
    private ImageButton mBuscarAdoptanteButton;
    private ImageView mGatoImageView, mDocumentoImageView;
    private CheckBox mAdoptanteCheckBox;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final int REQUEST_DOCUMENTO = 0;
    private static final int REQUEST_FOTO = 1;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Persona mAdoptante;
    private Gato mGato;
    private File mPhotoFile;
    private Uri mPhotoUri;
    private RegistroAdopcion mRegistroAdopcion;
    private UUID adopcionId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vista_adopcion_fragment, null);
        mNombreGatoEditText = v.findViewById(R.id.NombreGato);
        mNombrePersonaEditText = v.findViewById(R.id.nombre_responsable);
        mApellidoPaternoEditText = v.findViewById(R.id.apellido_paterno_responsable);
        mApellidoMaternoEditText = v.findViewById(R.id.apellido_materno_responsable);
        mDomicilioEditText = v.findViewById(R.id.domicilio_responable);
        mCelularEditText = v.findViewById(R.id.celular_responsable);
        mAdoptanteCheckBox = v.findViewById(R.id.responsable);
        mFormularioAdoptanteScrollView = v.findViewById(R.id.scrollView3);
        mBuscarAdoptanteButton = v.findViewById(R.id.buscar_responsable);
        mEmailEditText = v.findViewById(R.id.email_responsable);
        mGatoImageView = v.findViewById(R.id.materialImagen);
        mDocumentoButton = v.findViewById(R.id.documento);
        mDocumentoImageView = v.findViewById(R.id.imageDocumento);
        mVistaAdopcion = v.findViewById(R.id.vistaAdopcion);
        mGuardarButton = v.findViewById(R.id.guardar);
        mAgregaAdoptanteTextView = v.findViewById(R.id.agregaAdoptante);



        adopcionId = (UUID) getArguments().getSerializable("ADOPCION_ID");
        mGato = CatLab.get(getActivity()).getmGato(adopcionId);
        mRegistroAdopcion =RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(mGato.getmIdGato());
        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        putImageView(mGatoImageView);
        if (mGato.ismSexo().equals("Hembra")){
            mVistaAdopcion.setBackgroundColor(getResources().getColor(R.color.light_pink));
        }

        PackageManager packageManager = getActivity().getPackageManager();

        //boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mDocumentoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = RegistroAdopcionStorage.get(getActivity()).getPhotoFile(mRegistroAdopcion);
                mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
                //Se obtiene el uri de la imagen con su ruta especifica para poder guardar el archivo una vez que la foto se haya tomado
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                List<ResolveInfo> cameraActivities = getActivity().
                        getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                //Se otorga el permiso siempre y cuando la actividad tenga permitido usarla y esta no este usada por otras actividades
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission("activity.activityInfo", mPhotoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //Se llama a la camara para poder obtener su resultado
                startActivityForResult(captureImage, REQUEST_FOTO);
            }
        });

        if (mRegistroAdopcion!= null){
            mAdoptante = PersonaStorage.get(getActivity()).getmPersona(mRegistroAdopcion.getmAdoptanteId());
            mGuardarButton.setText("Actualizar");
            AdoptanteDefinido();
            mPhotoFile = RegistroAdopcionStorage.get(getActivity()).getPhotoFile(mRegistroAdopcion);
            mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
            putImageView(mDocumentoImageView);
        }else {
            mRegistroAdopcion = new RegistroAdopcion();
            mAdoptante = new Persona();
        }

        mAdoptanteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mFormularioAdoptanteScrollView.setVisibility(View.VISIBLE);
                    mAgregaAdoptanteTextView.setVisibility(View.GONE);

                    if(!mNombrePersonaEditText.getText().toString().isEmpty()){
                        mAdoptante.setmNombre(mNombrePersonaEditText.getText().toString());
                    }
                    if(!mApellidoMaternoEditText.getText().toString().isEmpty()){
                        mAdoptante.setmApellidoMaterno(mApellidoMaternoEditText.getText().toString());
                    }
                    if(!mApellidoPaternoEditText.getText().toString().isEmpty()){
                        mAdoptante.setmApellidoPaterno(mApellidoPaternoEditText.getText().toString());
                    }
                    if(!mEmailEditText.getText().toString().isEmpty()){
                        mAdoptante.setmEmail(mEmailEditText.getText().toString());
                    }
                    if(!mCelularEditText.toString().isEmpty()){
                        mAdoptante.setmCelular(mCelularEditText.getText().toString());
                    }
                    if(!mDomicilioEditText.getText().toString().isEmpty()){
                        mAdoptante.setmDomicilio(mDomicilioEditText.getText().toString());
                    }
                }
                else {
                    mFormularioAdoptanteScrollView.setVisibility(View.GONE);
                }
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
                mAdoptante.setmNombre(s.toString());
                if(mAdoptante.getmNombre().isEmpty()){
                    mAdoptante.setmNombre(null);
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
                mAdoptante.setmApellidoPaterno(s.toString());
                if(mAdoptante.getmApellidoPaterno().isEmpty()){
                    mAdoptante.setmApellidoPaterno(null);
                }
            }
        });
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdoptante.setmEmail(s.toString());
                if(mAdoptante.getmEmail().isEmpty()){
                    mAdoptante.setmEmail(null);
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
                mAdoptante.setmApellidoMaterno(s.toString());
                if(mAdoptante.getmApellidoMaterno().isEmpty()){
                    mAdoptante.setmApellidoMaterno(null);
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
                mAdoptante.setmDomicilio(s.toString());
                if(mAdoptante.getmDomicilio().isEmpty()){
                    mAdoptante.setmDomicilio(null);
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
                mAdoptante.setmCelular(s.toString());
                if(mAdoptante.getmCelular().isEmpty()){
                    mAdoptante.setmCelular(null);
                }
            }
        });


        mGuardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificacion()){
                    if (mAdoptanteCheckBox.isChecked()){
                        mRegistroAdopcion.setmGatoId(mGato.getmIdGato());
                        mRegistroAdopcion.setmAdoptanteId(mAdoptante.getmIdPersona());
                        GatoHogar mGatoHogar;
                        if (PersonaStorage.get(getActivity()).getmPersona(mAdoptante.getmIdPersona())==null){
                            mGatoHogar = new GatoHogar(mGato.getmIdGato());
                            PersonaStorage.get(getActivity()).addPersona(mAdoptante);
                            mGatoHogar.setmPersonaId(mAdoptante.getmIdPersona());
                            GatoHogarLab.get(getActivity()).addGatoHogar(mGatoHogar, getActivity());
                            mGato.setmProcedencia(2);
                            CatLab.get(getActivity()).updateGato(mGato);
                        }else {
                            PersonaStorage.get(getActivity()).updatePersona(mAdoptante);
                            if (GatoHogarLab.get(getActivity()).getmGatoHogar(mGato.getmIdGato())!=null){
                                mGatoHogar = GatoHogarLab.get(getActivity()).getmGatoHogar(mGato.getmIdGato());
                                GatoHogarLab.get(getActivity()).updateGatoHogar(mGatoHogar);
                            }else{
                                mGatoHogar = new GatoHogar(mGato.getmIdGato());
                                GatoHogarLab.get(getActivity()).addGatoHogar(mGatoHogar, getActivity());
                            }
                        }
                        if (RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(mGato.getmIdGato()) == null){
                            RegistroAdopcionStorage.get(getActivity()).addRegistroAdopcion(mRegistroAdopcion,getActivity());
                        }else {
                            RegistroAdopcionStorage.get(getActivity()).updateRegistroAdopcion(mRegistroAdopcion);
                        }
                    }else {
                        if (RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(mGato.getmIdGato()) != null) {
                            RegistroAdopcionStorage.get(getActivity()).deleteRegistroAdopcion(GPTDbSchema.RegistroAdopcionTable.Cols.UUID + "= ?", new String[]{mRegistroAdopcion.getmRegistroAdopcionId().toString()});
                            if (GatoHogarLab.get(getActivity()).getmGatoHogar(mGato.getmIdGato()) != null) {
                                GatoHogarLab.get(getActivity()).deleteGatoHogar(GPTDbSchema.GatoHogarTable.Cols.FKUUID_GATO + "= ?", new String[]{mGato.getmIdGato().toString()});
                            }
                        }
                    }
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(), "Datos incompletos",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        mBuscarAdoptanteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Busqueda();
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        mGato = CatLab.get(getActivity()).getmGato(adopcionId);
        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        putImageView(mGatoImageView);
        super.onResume();

    }

    private boolean verificacion(){
        boolean validacionDatos = true;

        if((mAdoptanteCheckBox.isChecked() && (mAdoptante.getmNombre() == null || mAdoptante.getmCelular() == null))){
            validacionDatos = false;
        }
        return validacionDatos;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_adopcion,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editar_gato:
                Intent intent = new Intent(getContext(), AdopcionesActivity.class);
                intent.putExtra("TYPE",true);
                intent.putExtra("GATO_ID", mGato.getmIdGato());
                startActivity(intent);
                return true;
            case R.id.borrar_adopcion:
                final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(getActivity());
                mDeleteDialog.setTitle("Borrar Adopcion")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Estas segura de borrar la z")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AdopcionStorage.get(getActivity()).deleteAdopcion(GPTDbSchema.AdopcionTable.Cols.FKUUID_GATO + "= ?", new String[]{adopcionId.toString()});
                                        if (mRegistroAdopcion!=null) {
                                            RegistroAdopcionStorage.get(getActivity()).deleteRegistroAdopcion(GPTDbSchema.RegistroAdopcionTable.Cols.UUID + "= ?", new String[]{mRegistroAdopcion.getmRegistroAdopcionId().toString()});
                                            GatoHogarLab.get(getActivity()).deleteGatoHogar(GPTDbSchema.GatoHogarTable.Cols.FKUUID_GATO + "= ?", new String[]{mGato.getmIdGato().toString()});
                                        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Busqueda (){
        Bundle arguments = new Bundle();
        FragmentManager manager = getFragmentManager();
        Busqueda dialog = new Busqueda();
        String query = "SELECT * FROM gatos WHERE NOT EXISTS (SELECT * FROM esterilizaciones WHERE gatos.uuid = esterilizaciones.gato_id)";
        arguments.putSerializable("TITLE", "Responsable");
        arguments.putSerializable("QUERY",query);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(VistaAdopcionFragment.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }

    private void AdoptanteDefinido(){
        mAdoptanteCheckBox.setChecked(true);
        mNombrePersonaEditText.setText(mAdoptante.getmNombre());
        mFormularioAdoptanteScrollView.setVisibility(View.VISIBLE);
        mAgregaAdoptanteTextView.setVisibility(View.GONE);
        mApellidoPaternoEditText.setText(mAdoptante.getmApellidoPaterno());
        mApellidoMaternoEditText.setText(mAdoptante.getmApellidoMaterno());
        mEmailEditText.setText(mAdoptante.getmEmail());
        mDomicilioEditText.setText(mAdoptante.getmDomicilio());
        mCelularEditText.setText(mAdoptante.getmCelular());
    }

    private void putImageView(ImageView mImageView) {
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPhotoUri);
            //Asignacion de orientacion correcta para la foto
            ExifInterface exif = null;
            exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            //obtiene la orientacion de la foto
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //Envia la orientacion y el bitmap como parametros para modificarlos
            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
            //Una vez adecuada la foto, se coloca en el imageView
            mImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_BUSQUEDA){
                UUID personaId = (UUID) data.getSerializableExtra(Busqueda.EXTRA_PERSONA_ID);
                mAdoptante = PersonaStorage.get(getActivity()).getmPersona(personaId);
                AdoptanteDefinido();
            }else if(requestCode == REQUEST_FOTO ){
            mPhotoFile = RegistroAdopcionStorage.get(getActivity()).getPhotoFile(mRegistroAdopcion);
            mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
            putImageView(mDocumentoImageView);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
