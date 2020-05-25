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

import java.util.Date;
import java.util.UUID;

public class EsterilizacionFragment extends Fragment {

    private String  mNombreGato, mNombrePersona, mApellidoP, mApellidoM, mCondicionEspecial, mDomicilio, mCelular, mEmail, mProcedencia, mSexo;
    private int mPeso, mAnticipo, mCostoExtra, mPrecio;
    private Long mfecha;
    private Date mFechaNacimiento;
    private CheckBox mFajaCheckBox, mAnticipoCheckBox;
    private EditText mAnticipoEditText, mCostoExtraEditText, mPrecioEditText;
    private TextView mCostoTotalTextView;
    private Button mTerminarRegistroButton;
    private UUID campañaId, gatoId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

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
                mAnticipo = count;
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
                mCostoExtra = count;
                int cantidad = mCostoExtra + mPrecio;
                mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPrecioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPrecio = count;
                int cantidad = mCostoExtra + mPrecio;
                mCostoTotalTextView.setText(getString(R.string.costo_total, cantidad));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        mTerminarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EsterilizacionStorage esterilizacionStorage = EsterilizacionStorage.get(getContext());
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
