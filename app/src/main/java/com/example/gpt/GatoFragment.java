package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class GatoFragment extends Fragment {

    Fragment fragment = new EsterilizacionFragment();


    Spinner mProcedenciaSpinner;
    EditText mNombreGatoEditText, mNombrePersonaEditText, mPesoEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText, mCondicionEditText, mEmailEditText;
    CheckBox mResponsableCheckBox, mCondicionEspecialCheckBox;
    RadioGroup mSexoRadioGroup;
    RadioButton mSexoRadioButton;
    ConstraintLayout mFormularioResponsableConstraintLayout;
    Button mBuscarResponsableButton, mBuscarGatoButton, mSiguienteButton;
    String mPeso, mNombreGato, mCondicionEspecial, mNombrePersona, mApellidoM, mApellidoP, mCelular, mEmail, mDomicilio, mProcedencia, mSexo;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Bundle arguments = new Bundle();
    private String mTitle;
    String [] mProcedenciaList = {"Recien rescatado", "Feral", "Propio"};

    UUID campañaId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        campañaId = (UUID) getArguments().getSerializable("CAMPAÑA_ID");
        final View view = inflater.inflate(R.layout.gato_fragment, null);
        mProcedenciaSpinner = view.findViewById(R.id.procedencia);
        mNombreGatoEditText = view.findViewById(R.id.nombre_gato);
        mNombrePersonaEditText = view.findViewById(R.id.nombre_responsable);
        mApellidoPaternoEditText = view.findViewById(R.id.apellido_paterno_responsable);
        mApellidoMaternoEditText = view.findViewById(R.id.apellido_materno_responsable);
        mPesoEditText = view.findViewById(R.id.peso);
        mDomicilioEditText = view.findViewById(R.id.domicilio_responable);
        mCelularEditText = view.findViewById(R.id.celular_responsable);
        mCondicionEditText = view.findViewById(R.id.condicion);
        mResponsableCheckBox = view.findViewById(R.id.responsable);
        mCondicionEspecialCheckBox = view.findViewById(R.id.condicion_especial);
        mFormularioResponsableConstraintLayout = view.findViewById(R.id.formulario_responsable);
        mBuscarGatoButton = view.findViewById(R.id.buscar_gato);
        mBuscarResponsableButton = view.findViewById(R.id.buscar_responsable);
        mSexoRadioGroup = view.findViewById(R.id.sexo);
        mSiguienteButton = view.findViewById(R.id.siguiente);
        mEmailEditText = view.findViewById(R.id.email_responsable);

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
                    mCondicionEditText.setVisibility(View.GONE);
                }

            }
        });

        mCondicionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCondicionEspecial = s.toString();
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
                mNombrePersona = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mApellidoPaternoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mApellidoP = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail = s.toString();
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
                mApellidoM = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDomicilioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDomicilio = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCelularEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCelular = s.toString();
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
                mNombreGato=s.toString();
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
                mPeso = s.toString();
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
                mNombrePersona = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArrayAdapter <String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mProcedenciaList);
        mProcedenciaSpinner.setAdapter(mAdapter);

        mProcedenciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 mProcedencia = mProcedenciaSpinner.getSelectedItem().toString();
                if (mProcedencia.equals(mProcedenciaList[0]) || mProcedencia.equals(mProcedenciaList[2])){
                    mResponsableCheckBox.setVisibility(View.VISIBLE);
                }else{
                    mResponsableCheckBox.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSiguienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(verificacion()){
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            Bundle arguments = new Bundle();
                            arguments.putString("NOMBRE_GATO",mNombreGato);
                            arguments.putString("PESO", mPeso);
                            arguments.putString("CONDICION_GATO", mCondicionEspecial);
                            arguments.putString("PROCEDENCIA", mProcedencia);
                            arguments.putString("SEXO", mSexo);
                            arguments.putString("NOMBRE_RESPONSABLE", mNombrePersona);
                            arguments.putString("APELLIDO_PATERNO", mApellidoP);
                            arguments.putString("APELLIDO_MATERNO", mApellidoM);
                            arguments.putString("DOMICILIO", mDomicilio);
                            arguments.putString("EMAIL", mEmail);
                            arguments.putString("CELULAR", mCelular);
                            arguments.putSerializable("CAMPAÑA_ID", campañaId);
                            fragment.setArguments(arguments);

                            fm.beginTransaction()
                                    .addToBackStack("DATOS_GATO")
                                    .replace(R.id.formulario_container, fragment)
                                    .commit();

                        }
                        else{
                            Toast.makeText(getActivity(), "Datos incompletos",
                                    Toast.LENGTH_LONG).show();
                        }
            }
        });

        mSexoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSexoRadioButton = view.findViewById(mSexoRadioGroup.getCheckedRadioButtonId());
                mSexo = mSexoRadioButton.getText().toString();

            }
        });

        return view;
    }


    private void Busqueda (){
        FragmentManager manager = getFragmentManager();
        Busqueda dialog = new Busqueda();
        arguments.putSerializable("TITLE", mTitle);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(GatoFragment.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }


    private boolean verificacion(){
        boolean validacionDatos = true;

        if(mResponsableCheckBox.isChecked() && (mNombrePersona == null || mApellidoM == null || mApellidoP == null || mCelular == null || mDomicilio == null || mEmail == null)){
            validacionDatos = false;
        }
        if(mCondicionEspecialCheckBox.isChecked() && (mCondicionEspecial == null)){
            validacionDatos = false;
        }
        if(mNombreGato == null || mPeso == null || mSexo == null || mProcedencia == null){
            validacionDatos = false;
        }
        return validacionDatos;
    }

}
