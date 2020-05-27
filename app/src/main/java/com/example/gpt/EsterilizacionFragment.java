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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.UUID;

public class EsterilizacionFragment extends Fragment {
    private CheckBox mFajaCheckBox, mAnticipoCheckBox, mCostoExtraCheckButton;
    private EditText mAnticipoEditText, mCostoExtraEditText, mPrecioEditText;
    private TextView mCostoTotalTextView;
    private Button mTerminarRegistroButton;
    private UUID campañaId;
    private EventBus bus = EventBus.getDefault();
    private Esterilizacion mEsterilizacion = new Esterilizacion();
    private Gato mGato;
    private Persona mPersona;
    private boolean objetoEnviadoPersona = false;
    private boolean objetoEnviadoEsterilizacion = false;
    private CatLab mCatLab;
    private GatoHogarLab mGatoHogarLab;
    private PersonaStorage mPersonaStorage;
    EsterilizacionStorage mEsterilizacionStorage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bus.register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mEsterilizacionStorage = EsterilizacionStorage.get(getActivity());
        campañaId = (UUID) getArguments().getSerializable("CAMPAÑA_ID");
        View view = inflater.inflate(R.layout.esterilizacion_datos_fragment, null);
        mFajaCheckBox = view.findViewById(R.id.faja);
        mAnticipoCheckBox = view.findViewById(R.id.anticipo);
        mAnticipoEditText = view.findViewById(R.id.anticipo_cantidad);
        mCostoExtraEditText = view.findViewById(R.id.costo_extra);
        mCostoTotalTextView = view.findViewById(R.id.costo_total);
        mPrecioEditText = view.findViewById(R.id.precio);
        mTerminarRegistroButton = view.findViewById(R.id.terminar_esterilizacion);
        mCostoExtraCheckButton = view.findViewById(R.id.costo_extra_check);

        if(objetoEnviadoEsterilizacion){
            mTerminarRegistroButton.setText(R.string.actualizar_datos);
            ColocarDatos();
        }

        mAnticipoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    mEsterilizacion.setmAnticipo(Integer.parseInt(mAnticipoEditText.getText().toString()));
                } catch (Exception e) {
                    mEsterilizacion.setmAnticipo(0);
                    e.printStackTrace();
                }
            }
        });

        mCostoExtraEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    mEsterilizacion.setmCostoExtra(Integer.parseInt(mCostoExtraEditText.getText().toString()));
                    int cantidad = mEsterilizacion.getmCostoExtra() + mEsterilizacion.getmPrecio();
                    mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
                } catch (Exception e) {
                    e.printStackTrace();
                    mEsterilizacion.setmCostoExtra(0);
                    int cantidad = mEsterilizacion.getmPrecio();
                    mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
                }

            }
        });
        mPrecioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    mEsterilizacion.setmPrecio(Integer.parseInt(mPrecioEditText.getText().toString()));
                    int cantidad =  mEsterilizacion.getmPrecio() + mEsterilizacion.getmCostoExtra();
                    mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
                } catch (Exception e) {
                    mEsterilizacion.setmPrecio(0);
                    int cantidad = mEsterilizacion.getmCostoExtra();
                    mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
                    e.printStackTrace();
                }
            }
        });

        mAnticipoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mAnticipoEditText.setVisibility(View.VISIBLE);
                }else {
                    mAnticipoEditText.setVisibility(View.GONE);
                }
            }
        });

        mFajaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mEsterilizacion.setmFaja(isChecked);
            }
        });

        mCostoExtraCheckButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCostoExtraEditText.setVisibility(View.VISIBLE);
                }else {
                    mCostoExtraEditText.setVisibility(View.GONE);
                }
            }
        });



        mTerminarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCatLab = CatLab.get(getActivity());
                mGatoHogarLab = GatoHogarLab.get(getActivity());
                mPersonaStorage = PersonaStorage.get(getActivity());
                if(objetoEnviadoEsterilizacion){
                    actualizarDatos();
                    getActivity().finish();
                }
                else{
                    if(mGato.isValidacion() && mEsterilizacion.getmPrecio() != 0){
                        crearDatos();
                        getActivity().finish();
                    }
                    else{
                        Toast.makeText(getActivity(), "Datos incompletos",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
        }

    private void ColocarDatos() {
        mPrecioEditText.setText(Integer.toString(mEsterilizacion.getmPrecio()));
        if(mEsterilizacion.getmCostoExtra() != 0){
            mCostoExtraEditText.setText(Integer.toString(mEsterilizacion.getmCostoExtra()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe
    public void recibirGato(Gato gato){
        mGato = gato;
    }

    public void crearDatos(){
        if(mCatLab.getmGato(mGato.getmIdGato())==null){
            mCatLab.addGato(mGato, getActivity());
        }

        if(objetoEnviadoPersona){
            if(mPersonaStorage.getmPersona(mPersona.getmIdPersona()) == null){
                mPersonaStorage.addPersona(mPersona, getActivity());
            }

            if(mGatoHogarLab.getmGatoHogar(mGato.getmIdGato()) == null){
                GatoHogar mGatoHogar = new GatoHogar(mGato.getmIdGato());
                mGatoHogar.setmPersonaId(mPersona.getmIdPersona());
                mGatoHogarLab.addGatoHogar(mGatoHogar, getActivity());
            }
        }
        mEsterilizacion.setmIdCampaña(campañaId);
        mEsterilizacion.setmIdGato(mGato.getmIdGato());
        mEsterilizacionStorage.addEsterilizacion(mEsterilizacion, getActivity());
    }

    public void actualizarDatos(){
        GatoHogar mGatoHogar = new GatoHogar(mGato.getmIdGato());
        if(objetoEnviadoPersona){

            if(mPersonaStorage.getmPersona(mPersona.getmIdPersona()) == null){
                mPersonaStorage.addPersona(mPersona, getActivity());
            }

            if(mGatoHogarLab.getmGatoHogar(mGato.getmIdGato()) == null){
                mGatoHogar.setmPersonaId(mPersona.getmIdPersona());
                mGatoHogarLab.addGatoHogar(mGatoHogar, getActivity());
            }
            else {
                mGatoHogar.setmPersonaId(mPersona.getmIdPersona());
            }
            mPersonaStorage.updatePersona(mPersona);
            mGatoHogarLab.updateGatoHogar(mGatoHogar);
        }
        else{
            if(mGatoHogarLab.getmGatoHogar(mGato.getmIdGato()) != null){
                mGatoHogarLab.deleteGatoHogar(GPTDbSchema.GatoHogarTable.Cols.FKUUID_GATO + " = ?", new String[]{mGato.getmIdGato().toString()});
            }
        }
        mCatLab.updateGato(mGato);
        mEsterilizacionStorage.updateEsterilizacion(mEsterilizacion);
    }

    @Subscribe
    public void recibirEsterilizacion(Esterilizacion esterilizacion){
        mEsterilizacion = esterilizacion;
        objetoEnviadoEsterilizacion = true;
    }

    @Subscribe
    public void recibirResponsable(Persona persona){
        mPersona = persona;
        objetoEnviadoPersona = true;
    }
}
