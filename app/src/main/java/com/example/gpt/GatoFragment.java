package com.example.gpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class GatoFragment extends Fragment {

    Spinner mProcedenciaSpinner;
    EditText mNombreGatoEditText, mNombrePersonaEditText, mPesoEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText, mCondicionEditText;
    CheckBox mResponsableCheckBox, mCondicionEspecialCheckBox;
    ConstraintLayout mFormularioResponsableConstraintLayout;

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


        String [] procedencia = {"Recien rescatado", "Feral", "Propio"};
        ArrayAdapter <String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, procedencia);
        mProcedenciaSpinner.setAdapter(mAdapter);
        return view;
    }

}
