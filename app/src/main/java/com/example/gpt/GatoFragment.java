package com.example.gpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class GatoFragment extends Fragment {

    Spinner mProcedenciaSpinner;
    EditText mNombreGatoEditText, mNombrePersonaEditText, mPesoEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText, mCondicionEditText;
    CheckBox mResponsableCheckBox, mCondicionEspecialCheckBox;
    ConstraintLayout mFormularioResponsableConstraintLayout;
    Button mBuscarResponsableButton, mBuscarGatoButton;
    Boolean mCondicion, mResponsable;
    String mPeso, mNombreGato, mCondicionEspecial, mNombrePersona, mApellidoM, mApellidoP, mCelular, mEmail, mDomicilio, mProcedencia;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Bundle arguments = new Bundle();
    private String mTitle;
    String [] mProcedenciaList = {"Recien rescatado", "Feral", "Propio"};;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gato_fragment, null);
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

        ArrayAdapter <String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mProcedenciaList);
        mProcedenciaSpinner.setAdapter(mAdapter);

        mProcedenciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String procedencia = mProcedenciaSpinner.getSelectedItem().toString();
                if (procedencia.equals(mProcedenciaList[0])|| procedencia.equals(mProcedenciaList[2])){
                    mResponsableCheckBox.setVisibility(View.VISIBLE);
                }else{
                    mResponsableCheckBox.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

}
