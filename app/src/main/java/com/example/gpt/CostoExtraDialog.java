package com.example.gpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.UUID;

public class CostoExtraDialog extends DialogFragment {
    public static final String CAMPAÑA_ID = "campañaId";
    private EditText mDescripcionEditText, mCantidadEditText;
    private TextView mFechaActual;
    private CostoExtra mCostoExtra;
    private AlertDialog dialog;
    private UUID mCostoExtraId, mPensionId;
    boolean nuevaInstancia = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.costo_extra_dialog,null);
        mDescripcionEditText = view.findViewById(R.id.descripcion);
        mCantidadEditText = view.findViewById(R.id.cantidad);
        mFechaActual = view.findViewById(R.id.fechaActual);
        nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");
        mPensionId = (UUID) getArguments().getSerializable("PENSION_ID");
        if (nuevaInstancia){
           mCostoExtra = new CostoExtra();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Costo Extra")
                .setIcon(R.drawable.billete)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCostoExtra.setmDescripcion(mDescripcionEditText.getText().toString());
                        mCostoExtra.setmCantidad(Integer.parseInt(mCantidadEditText.getText().toString()));
                        mCostoExtra.setmPensionId(mPensionId);
                        if (nuevaInstancia){
                            CostoExtraStorage.get(getActivity()).addCostoExtra(mCostoExtra, getActivity());
                        }else{
                            CostoExtraStorage.get(getActivity()).updateCostoExtra(mCostoExtra);
                        }
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    }
                })
                .setNeutralButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CostoExtraStorage.get(getActivity()).deleteCostoExtra(GPTDbSchema.CostoExtraTable.Cols.UUID + "= ?", new String[]{mCostoExtra.getmCostoExtraId().toString()});
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        mDescripcionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog = (AlertDialog) getDialog();
                if(mDescripcionEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        mCantidadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dialog = (AlertDialog) getDialog();
                if(mDescripcionEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = (AlertDialog) getDialog();
        if(!nuevaInstancia) {
            mCostoExtraId = (UUID) getArguments().getSerializable("COSTOEXTRA_ID");
            mCostoExtra = CostoExtraStorage.get(getActivity()).getmCostoExtra(mCostoExtraId);
            mDescripcionEditText.setText(mCostoExtra.getmDescripcion());
            String cantidad = Integer.toString(mCostoExtra.getmCantidad());
            mCantidadEditText.setText(cantidad);
            mFechaActual.setText(mCostoExtra.getDateFormat());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
        }else{
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
        }
    }
}
