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
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class CrearRecursosDialog extends DialogFragment {

    public static final String CAMPAÑA_ID = "campañaId";
    private DatePicker mDatePicker;
    private EditText mMotivoEditText, mCantidadEditText;
    private Ingreso mRecurso;
    private AlertDialog dialog;
    private UUID recursoId;
    boolean nuevaInstancia = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.recurso_dialog,null);
        mDatePicker = view.findViewById(R.id.date_picker_campaña);
        mMotivoEditText = view.findViewById(R.id.motivo);
        mCantidadEditText = view.findViewById(R.id.cantidad);
        nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");
        if (nuevaInstancia){
            mRecurso =new Ingreso();
        }
        final Boolean type = getArguments().getBoolean("TYPE");
        String mTitle = "Gasto";
        if (type){
            mTitle = "Ingreso";
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(mTitle)
                .setIcon(R.drawable.coins)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        mRecurso.setMotivo(mMotivoEditText.getText().toString());
                        mRecurso.setmFecha(date);
                        mRecurso.setmCantidad(Integer.parseInt(mCantidadEditText.getText().toString()));
                        if (nuevaInstancia){
                            if (type){
                                IngresoBank.get(getActivity()).addIngreso(mRecurso,getActivity(),GPTDbSchema.IngresoTable.NAME);
                            }else{
                                IngresoBank.get(getActivity()).addIngreso(mRecurso,getActivity(),GPTDbSchema.GastoTable.NAME);
                            }
                        }else{
                            if (type){
                                IngresoBank.get(getActivity()).updateIngreso(mRecurso,GPTDbSchema.IngresoTable.NAME,GPTDbSchema.IngresoTable.Cols.UUID);
                            }else{
                                IngresoBank.get(getActivity()).updateIngreso(mRecurso,GPTDbSchema.GastoTable.NAME,GPTDbSchema.GastoTable.Cols.UUID);
                            }

                        }
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    }
                })
                .setNeutralButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type){
                            IngresoBank.get(getActivity()).deleteIngreso(GPTDbSchema.IngresoTable.Cols.UUID + "= ?", new String[]{mRecurso.getmIdIngreso().toString()},GPTDbSchema.IngresoTable.NAME);
                        }else{
                            IngresoBank.get(getActivity()).deleteIngreso(GPTDbSchema.IngresoTable.Cols.UUID + "= ?", new String[]{mRecurso.getmIdIngreso().toString()},GPTDbSchema.GastoTable.NAME);
                        }
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

        mMotivoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog = (AlertDialog) getDialog();
                if(mMotivoEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
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
                if(mMotivoEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
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
            recursoId = (UUID) getArguments().getSerializable("RECURSO_ID");
            mRecurso = IngresoBank.get(getActivity()).getmIngreso(recursoId);
            mMotivoEditText.setText(mRecurso.getMotivo());
            String cantidad = Integer.toString(mRecurso.getmCantidad());
            mCantidadEditText.setText(cantidad);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(mRecurso.getmFecha());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year, month, day,null);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);

        }else{
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }
}
