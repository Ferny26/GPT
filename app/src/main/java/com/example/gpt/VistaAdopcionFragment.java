package com.example.gpt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class VistaAdopcionFragment extends Fragment {

    private EditText mNombreGatoEditText, mNombrePersonaEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText, mEmailEditText;
    private ConstraintLayout mFormularioAdoptanteConstraintLayout;
    private Button mDocumentoButton, mGuardarButton;
    private ImageButton mBuscarAdoptanteButton;
    private ImageView mGatoImageView, mDocumentoImageView;
    private CheckBox mAdoptanteCheckBox;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final int REQUEST_DOCUMENTO = 0;
    private static final int REQUEST_FOTO = 1;
    private String mTitle;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Persona mAdoptante;
    private Gato mGato;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        mFormularioAdoptanteConstraintLayout = v.findViewById(R.id.formulario_responsable);
        mBuscarAdoptanteButton = v.findViewById(R.id.buscar_responsable);
        mEmailEditText = v.findViewById(R.id.email_responsable);
        mGatoImageView = v.findViewById(R.id.materialImagen);
        mDocumentoButton = v.findViewById(R.id.documento);
        mDocumentoImageView = v.findViewById(R.id.imageDocumento);
        mGuardarButton = v.findViewById(R.id.guardar);


        UUID adopcionId = (UUID) getArguments().getSerializable("ADOPCION_ID");
        final RegistroAdopcion mRegistroAdopcion = RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(adopcionId);
        mGato = CatLab.get(getActivity()).getmGato(adopcionId);

        mNombreGatoEditText.setText(mGato.getmNombreGato());


        if (mRegistroAdopcion!= null){
            mAdoptante = PersonaStorage.get(getActivity()).getmPersona(mRegistroAdopcion.getmAdoptanteId());
            mGuardarButton.setText("Actualizar");
            AdoptanteDefinido();
        }else {
            mAdoptante = new Persona();
        }

        mAdoptanteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mFormularioAdoptanteConstraintLayout.setVisibility(View.VISIBLE);
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
                    mFormularioAdoptanteConstraintLayout.setVisibility(View.GONE);
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
                        mRegistroAdopcion.setmEstatus(1);
                        mRegistroAdopcion.setmGatoId(mGato.getmIdGato());
                        mRegistroAdopcion.setmAdoptanteId(mAdoptante.getmIdPersona());
                        if (RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(mRegistroAdopcion.getmRegistroAdopcionId()) == null){
                            RegistroAdopcionStorage.get(getActivity()).addRegistroAdopcion(mRegistroAdopcion,getActivity());
                        }else {
                            RegistroAdopcionStorage.get(getActivity()).updateRegistroAdopcion(mRegistroAdopcion);
                        }
                        if (PersonaStorage.get(getActivity()).getmPersona(mAdoptante.getmIdPersona())==null){
                            PersonaStorage.get(getActivity()).addPersona(mAdoptante);
                        }else {
                            PersonaStorage.get(getActivity()).updatePersona(mAdoptante);
                        }
                    }else if (RegistroAdopcionStorage.get(getActivity()).getmRegistroAdopcion(mRegistroAdopcion.getmRegistroAdopcionId()) != null){
                            //RegistroAdopcionStorage.get(getActivity()).deleteRegistroAdopcion(mRegistroAdopcion.getmRegistroAdopcionId());
                    }
                }
            }
        });


        return v;
    }

    private boolean verificacion(){
        boolean validacionDatos = true;

        if((mAdoptanteCheckBox.isChecked() && (mAdoptante.getmNombre() == null || mAdoptante.getmCelular() == null))){
            validacionDatos = false;
        }
        return validacionDatos;
    }
    
    private void Busqueda (){
        Bundle arguments = new Bundle();
        FragmentManager manager = getFragmentManager();
        Busqueda dialog = new Busqueda();
        String query = "SELECT * FROM gatos WHERE NOT EXISTS (SELECT * FROM esterilizaciones WHERE gatos.uuid = esterilizaciones.gato_id)";
        arguments.putSerializable("TITLE", "Persona");
        arguments.putSerializable("QUERY",query);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(VistaAdopcionFragment.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }

    private void AdoptanteDefinido(){
        /*mResponsableCheckBox.setChecked(true);
        mNombrePersonaEditText.setText(mAdoptante.getmNombre());
        mFormularioResponsableConstraintLayout.setVisibility(View.VISIBLE);
        mApellidoPaternoEditText.setText(mAdoptante.getmApellidoPaterno());
        mApellidoMaternoEditText.setText(mAdoptante.getmApellidoMaterno());
        mEmailEditText.setText(mAdoptante.getmEmail());
        mDomicilioEditText.setText(mAdoptante.getmDomicilio());
        mCelularEditText.setText(mAdoptante.getmCelular());*/
    }
}
