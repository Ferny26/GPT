package com.example.gpt;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;


import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.UUID;

public class GatoFragment extends Fragment {

    private Spinner mProcedenciaSpinner;
    private NumberPicker  mMesNumberPicker, mAñoNumberPicker;
    private EditText mNombreGatoEditText, mNombrePersonaEditText, mPesoEditText, mDomicilioEditText, mApellidoPaternoEditText, mApellidoMaternoEditText, mCelularEditText;
    private EditText  mCondicionEditText, mEmailEditText;
    private CheckBox mResponsableCheckBox, mCondicionEspecialCheckBox;
    private RadioGroup mSexoRadioGroup;
    private TextView mFechaNacimientoTextView;
    private RadioButton mSexoRadioButton, mRadio;
    private Date mFechaNacimiento = new Date();
    private ConstraintLayout mFormularioResponsableConstraintLayout;
    private Button mBuscarResponsableButton, mBuscarGatoButton;
    private String  mTitle;
    private static final int REQUEST_BUSQUEDA = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Bundle arguments = new Bundle();
    private String [] mProcedenciaList = {"Recien rescatado", "Feral", "Propio"};
    private UUID campañaId, esterilizacionId;
    private Gato mGato;
    private Persona mResponsable;
    Esterilizacion mEsterilizacion;
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gato_fragment, null);
        boolean nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");
        if(!nuevaInstancia) {
            esterilizacionId = (UUID) getArguments().getSerializable("ESTERILIZACION_ID");
        }
        //////////////////////////////////////////////// Wiring Up /////////////////////////////////////////////////
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
        mEmailEditText = view.findViewById(R.id.email_responsable);
        mMesNumberPicker = view.findViewById(R.id.mes_select);
        mAñoNumberPicker = view.findViewById(R.id.fecha_año_select);
        ArrayAdapter <String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mProcedenciaList);
        mProcedenciaSpinner.setAdapter(mAdapter);
        mGato = new Gato();
        mResponsable = new Persona();

        if (esterilizacionId != null){
            EsterilizacionStorage mEsterilizacionStorage = EsterilizacionStorage.get(getActivity());
            mEsterilizacion = mEsterilizacionStorage.getEsterilizacion(esterilizacionId);
            CatLab mCatLab = CatLab.get(getActivity());
            mGato = mCatLab.getmGato(mEsterilizacion.getmIdGato());
            GatoDefinido();
            GatoHogarLab mgatoHogarLab = GatoHogarLab.get(getActivity());
            GatoHogar mgatoHogar = mgatoHogarLab.getmGatoHogar(mGato.getmIdGato());
            if (mgatoHogar != null){
                PersonaStorage mPersonaStorage = PersonaStorage.get(getActivity());
                mResponsable = mPersonaStorage.getmPersona(mgatoHogar.getmPersonaId());
                ResponsableDefinido();
            }

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////// Spinner ///////////////////////////////////////////////////////////////////////////////////////////////////
        //Seleccion de procedencia
        mProcedenciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Responsable activo para procedencia permitida
                if (position == 0 || position == 2){
                    mGato.setmProcedencia(position);
                    mResponsableCheckBox.setVisibility(View.VISIBLE);
                }else{
                    mResponsableCheckBox.setVisibility(View.GONE);
                    mFormularioResponsableConstraintLayout.setVisibility(View.GONE);
                    mGato.setmProcedencia(position);
                }
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

        mSexoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSexoRadioButton = view.findViewById(mSexoRadioGroup.getCheckedRadioButtonId());
                mGato.setmSexo(checkedId);

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
                mResponsable.setmNombre(s.toString());
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
                mResponsable.setmApellidoPaterno(s.toString());
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
                mResponsable.setmEmail(s.toString());
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
                mResponsable.setmApellidoMaterno(s.toString());
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
                mResponsable.setmDomicilio(s.toString());
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
                mResponsable.setmCelular(s.toString());
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
                mGato.setmNombreGato(s.toString());
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
                mGato.setmPeso(s.toString());
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
                mResponsable.setmNombre(s.toString());
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
        arguments.putSerializable("TITLE", mTitle);
        dialog.setArguments(arguments);
        dialog.setTargetFragment(GatoFragment.this, REQUEST_BUSQUEDA);
        dialog.show(manager,DIALOG_CREATE);
    }

    private void GatoDefinido(){
        mNombreGatoEditText.setText(mGato.getmNombreGato());
        mPesoEditText.setText(mGato.getmPeso());
        mMesNumberPicker.setValue(mGato.getmFechaNacimiento().getMonth());
        mAñoNumberPicker.setValue(mGato.getmFechaNacimiento().getYear());
        mRadio = (RadioButton) mSexoRadioGroup.getChildAt(mGato.ismSexo());
        mRadio.setPressed(true);
        if(mGato.getmCondicionEspecial() != null){
            mCondicionEspecialCheckBox.setChecked(true);
            mCondicionEditText.setVisibility(View.VISIBLE);
            mCondicionEditText.setText(mGato.getmCondicionEspecial());
        }
        mProcedenciaSpinner.setSelection(mGato.getmProcedencia());
    }

    private void ResponsableDefinido(){
        mResponsableCheckBox.setChecked(true);
        mNombrePersonaEditText.setText(mResponsable.getmNombre());
        mApellidoPaternoEditText.setText(mResponsable.getmApellidoPaterno());
        if(mResponsable.getmApellidoMaterno() == null){
            mApellidoMaternoEditText.setText(mResponsable.getmApellidoMaterno());
        }
        if(mResponsable.getmEmail() == null){
            mEmailEditText.setText(mResponsable.getmEmail());
        }
        mDomicilioEditText.setText(mResponsable.getmDomicilio());
        mCelularEditText.setText(mResponsable.getmCelular());
    }



    @Override
    public void onPause() {
        if(esterilizacionId!=null) {
            bus.post(mEsterilizacion);
        }
        mGato.setValidacion(verificacion());

        bus.post(mGato);
        if (mResponsableCheckBox.isChecked()) {
            bus.post(mResponsable);
        }
        super.onPause();
    }

    private boolean verificacion(){
        boolean validacionDatos = true;

        if(mResponsableCheckBox.isChecked() && (mResponsable.getmNombre() == null || mResponsable.getmApellidoPaterno() == null || mResponsable.getmCelular() == null || mResponsable.getmDomicilio() == null )){
            validacionDatos = false;
        }
        if(mCondicionEspecialCheckBox.isChecked() && (mGato.getmCondicionEspecial() == null)){
            validacionDatos = false;
        }
        if(mGato.getmNombreGato() == null || mGato.getmPeso() == null  || mFechaNacimiento == null){
            validacionDatos = false;
        }
        return validacionDatos;
    }

}
