package com.example.gpt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PensionDatosFragment extends Fragment {

    private NumberPicker mDiaIngresoNumberPicker, mMesIngresoNumberPicker, mAñoIngresoNumberPicker;
    private NumberPicker mDiaSalidaNumberPicker, mMesSalidaNumberPicker, mAñoSalidaNumberPicker;
    private EditText mPrecioDiarioEditText;
    private Button mTerminarRegistroButton, mPagadoButton;
    private Spinner mTipoPensionSpinner;
    private Pension mPension;
    private Date fechaSalida = new Date(), fechaIngreso = new Date();
    private Gato mGato;
    private String [] mTipoPensionList = {"Dueño", "Post-Quirurgica", "Protocolo"};
    private GatoHogar mGatoHogar;
    private Persona mResponsable;
    private PersonaStorage mPersonaStorage;
    private PensionStorage mPensionStorage;
    private CatLab mCatlab;
    private EventBus bus = EventBus.getDefault();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bus.register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPersonaStorage = PersonaStorage.get(getActivity());
        mPensionStorage = PensionStorage.get(getActivity());
        mCatlab = CatLab.get(getActivity());

        View view = inflater.inflate(R.layout.datos_pension_fragment, null);
        mTipoPensionSpinner = view.findViewById(R.id.categoria);
        mDiaIngresoNumberPicker = view.findViewById(R.id.dia_ingreso);
        mMesIngresoNumberPicker = view.findViewById(R.id.mes_ingreso);
        mTerminarRegistroButton = view.findViewById(R.id.terminar_pensión);
        mPagadoButton = view.findViewById(R.id.pagado);
        mAñoIngresoNumberPicker = view.findViewById(R.id.año_ingreso);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mTipoPensionList);
        mTipoPensionSpinner.setAdapter(mAdapter);



        mDiaSalidaNumberPicker = view.findViewById(R.id.dia_salida);
        mMesSalidaNumberPicker = view.findViewById(R.id.mes_salida);
        mAñoSalidaNumberPicker = view.findViewById(R.id.año_salida);

        mPrecioDiarioEditText = view.findViewById(R.id.costo_diario);
        mTerminarRegistroButton = view.findViewById(R.id.terminar_pensión);
        mPagadoButton = view.findViewById(R.id.pagado);

        mMesIngresoNumberPicker.setMinValue(1);
        mMesIngresoNumberPicker.setMaxValue(12);

        mMesSalidaNumberPicker.setMinValue(1);
        mMesSalidaNumberPicker.setMaxValue(12);

        mDiaIngresoNumberPicker.setMinValue(1);
        mDiaIngresoNumberPicker.setMaxValue(31);

        mDiaSalidaNumberPicker.setMinValue(1);
        mDiaSalidaNumberPicker.setMaxValue(31);

        mAñoIngresoNumberPicker.setMinValue(2020);
        mAñoIngresoNumberPicker.setMaxValue(2025);

        mAñoSalidaNumberPicker.setMinValue(2020);
        mAñoSalidaNumberPicker.setMaxValue(2025);

        if(mPensionStorage.getmPension(mPension.getmIdPension())!=null){
            mTerminarRegistroButton.setText(R.string.actualizar_datos);
            mostrarDatos();
        }

        mDiaIngresoNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });

        mMesIngresoNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int mes = mMesIngresoNumberPicker.getValue();
                if(mes % 2 == 1 && mes != 1){
                    mDiaIngresoNumberPicker.setMaxValue(30);
                }else{
                    mDiaIngresoNumberPicker.setMaxValue(31);
                }
                if(mes == 2 && mAñoIngresoNumberPicker.getValue() % 4 == 0){
                    mDiaIngresoNumberPicker.setMaxValue(29);
                }else if(mes == 2){
                    mDiaIngresoNumberPicker.setMaxValue(28);
                }
            }
        });

        mAñoIngresoNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int mes = mMesIngresoNumberPicker.getValue();
                if(mes == 2 && mAñoIngresoNumberPicker.getValue() % 4 == 0){
                    mDiaIngresoNumberPicker.setMaxValue(29);
                }else if(mes == 2){
                    mDiaIngresoNumberPicker.setMaxValue(28);
                }
            }
        });

        mDiaSalidaNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });

        mMesSalidaNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int mes = mMesSalidaNumberPicker.getValue();
                if(mes % 2 == 1 && mes != 1){
                    mDiaSalidaNumberPicker.setMaxValue(30);
                }else{
                    mDiaSalidaNumberPicker.setMaxValue(31);
                }
                if(mes == 2 && mAñoSalidaNumberPicker.getValue() % 4 == 0){
                    mDiaSalidaNumberPicker.setMaxValue(29);
                }else if(mes == 2){
                    mDiaSalidaNumberPicker.setMaxValue(28);
                }
            }
        });

        mAñoSalidaNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int mes = mMesSalidaNumberPicker.getValue();
                if(mes == 2 && mAñoSalidaNumberPicker.getValue() % 4 == 0){
                    mDiaSalidaNumberPicker.setMaxValue(29);
                }else if(mes == 2){
                    mDiaSalidaNumberPicker.setMaxValue(28);
                }
            }
        });

        mPrecioDiarioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    mPension.setmPrecioDia(Integer.parseInt(s.toString()));
                }else{
                    mPension.setmPrecioDia(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTipoPensionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPension.setmTipoPension(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mTerminarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int añoIngreso = mAñoIngresoNumberPicker.getValue();
                int mesIngreso = mMesIngresoNumberPicker.getValue();
                int diaIngreso = mDiaIngresoNumberPicker.getValue();
                int añoSalida = mAñoSalidaNumberPicker.getValue();
                int mesSalida = mMesSalidaNumberPicker.getValue();
                int diaSalida = mDiaSalidaNumberPicker.getValue();

                Date fechaIngreso = new GregorianCalendar(añoIngreso, mesIngreso-1, diaIngreso).getTime();
                Date fechaSalida = new GregorianCalendar(añoSalida, mesSalida-1, diaSalida).getTime();


                long diff = fechaSalida.getTime() - fechaIngreso.getTime() ;
                diff = (diff / (1000 * 60 * 60 * 24));
                boolean validacionDatos = true;
                if (diff <= 0) {
                    validacionDatos = false;
                    Toast.makeText(getActivity(), "Las fechas son invalidas",
                            Toast.LENGTH_LONG).show();
                }else{

                }
                if(mPension.getmPrecioDia() == 0){
                    if(validacionDatos){
                        Toast.makeText(getActivity(), "El ingreso es invalido",
                                Toast.LENGTH_LONG).show();
                    }

                    validacionDatos = false;
                }


                if(mPensionStorage.getmPension(mPension.getmIdPension())!=null && validacionDatos){
                    mPension.setmFechaSalida(fechaSalida);
                    mPension.setmFechaIngreso(fechaIngreso);
                    actualizarDatos();

                }else if(validacionDatos){
                    mPension.setmFechaSalida(fechaSalida);
                    mPension.setmFechaIngreso(fechaIngreso);
                    crearDatos();

                }

            }
        });


        return view;
    }

    private void mostrarDatos() {

        mPrecioDiarioEditText.setText(Integer.toString(mPension.getmPrecioDia()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mPension.getmFechaIngreso());

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int año = calendar.get(Calendar.YEAR);

        mDiaIngresoNumberPicker.setValue(dia);
        mMesIngresoNumberPicker.setValue(mes +1);
        mAñoIngresoNumberPicker.setValue(año);


        calendar.setTime(mPension.getmFechaSalida());

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);

        mDiaSalidaNumberPicker.setValue(dia);
        mMesSalidaNumberPicker.setValue(mes + 1);
        mAñoSalidaNumberPicker.setValue(año);

        mTipoPensionSpinner.setSelection(mPension.getmTipoPension());

    }

    private void crearDatos() {
        if (mGato.isValidacion()) {

            if (mPersonaStorage.getmPersona(mResponsable.getmIdPersona()) == null) {
                mPersonaStorage.addPersona(mResponsable);
            } else {
                mPersonaStorage.updatePersona(mResponsable);
            }

            if (mCatlab.getmGato(mGato.getmIdGato()) == null) {
                mCatlab.addGato(mGato, getActivity());
            } else {
                mCatlab.updateGato(mGato);
            }
            mPension.setmGatoId(mGato.getmIdGato());
            mPensionStorage.addPension(mPension);

            GatoHogar mGatoHogar = new GatoHogar(mGato.getmIdGato());
            mGatoHogar.setmPersonaId(mResponsable.getmIdPersona());
            if (GatoHogarLab.get(getActivity()).getmGatoHogar(mGatoHogar.getmGatoId()) == null) {
                GatoHogarLab.get(getActivity()).addGatoHogar(mGatoHogar, getActivity());
            } else {
                GatoHogarLab.get(getActivity()).updateGatoHogar(mGatoHogar);
            }
            getActivity().finish();
        }else{
            Toast.makeText(getActivity(), "Datos incompletos",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void actualizarDatos() {
        if (mGato.isValidacion()) {
            mPension.setmGatoId(mGato.getmIdGato());
            mPensionStorage.updatePension(mPension);
            mPersonaStorage.updatePersona(mResponsable);
            mCatlab.updateGato(mGato);
            GatoHogar mGatoHogar = new GatoHogar(mGato.getmIdGato());
            mGatoHogar.setmPersonaId(mResponsable.getmIdPersona());
            GatoHogarLab.get(getActivity()).updateGatoHogar(mGatoHogar);
            getActivity().finish();
        }else {
            Toast.makeText(getActivity(), "Datos incompletos",
                    Toast.LENGTH_LONG).show();

        }

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
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        bus.post(mResponsable);
        bus.post(mPension);
        bus.post(mGato);
        super.onPause();
    }
}
