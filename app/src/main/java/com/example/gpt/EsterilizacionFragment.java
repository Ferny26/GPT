package com.example.gpt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class EsterilizacionFragment extends Fragment {

    String mPeso, mNombreGato, mNombrePersona, mApellidoP, mApellidoM, mCondicionEspecial, mDomicilio, mCelular, mEmail, mProcedencia, mSexo, mAnticipo, mCostoExtra;
    CheckBox mFajaCheckBox, mAnticipoCheckBox;
    EditText mAnticipoEditText, mCostoExtraEditText;
    TextView mCostoTotalTextView;
    Button mTerminarRegistroButton;
    UUID campañaId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPeso = getArguments().getString("PESO");
        mNombreGato = getArguments().getString("NOMBRE_GATO");
        mNombrePersona = getArguments().getString("NOMBRE_RESPONSABLE");
        mApellidoP = getArguments().getString("APELLIDO_PATERNO");
        mApellidoM = getArguments().getString("APELLIDO_MATERNO");
        mCondicionEspecial = getArguments().getString("CONDICION_GATO");
        mDomicilio = getArguments().getString("DOMICILIO");
        mCelular = getArguments().getString("CELULAR");
        mEmail = getArguments().getString("EMAIL");
        mProcedencia = getArguments().getString("PROCEDENCIA");
        mSexo = getArguments().getString("SEXO");
        campañaId = (UUID) getArguments().getSerializable("CAMPAÑA_ID");


        View view = inflater.inflate(R.layout.esterilizacion_datos_fragment, null);
        mFajaCheckBox = view.findViewById(R.id.faja);
        mAnticipoCheckBox = view.findViewById(R.id.anticipo);
        mAnticipoEditText = view.findViewById(R.id.anticipo_cantidad);
        mCostoExtraEditText = view.findViewById(R.id.costo_extra);
        mCostoTotalTextView = view.findViewById(R.id.costo_total);
        mTerminarRegistroButton = view.findViewById(R.id.terminar_esterilizacion);

        mAnticipoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAnticipo = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }




        });

        mCostoExtraEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCostoExtra = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mTerminarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatLab catLab = CatLab.get(getContext());
                EsterilizacionStorage esterilizacionStorage = EsterilizacionStorage.get(getContext());
                Gato gato = new Gato();
                gato.setmNombreGato(mNombreGato);
                gato.setmPeso(Integer.parseInt(mPeso));
                gato.setmCondicionEspecial(mCondicionEspecial);
                gato.setmProcedencia(mProcedencia);
                gato.setmSexo(mSexo);
                catLab.addGato(gato, getContext());

                Esterilizacion esterilizacion = new Esterilizacion();
                esterilizacion.setmIdCampaña(campañaId);
                esterilizacion.setmIdGato(gato.getmIdGato());
                esterilizacionStorage.addEsterilizacion(esterilizacion, getContext());
                getActivity().finish();
            }
        });




        return view;


        }

}
