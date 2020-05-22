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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.GregorianCalendar;

public class CrearCampañaDialog extends DialogFragment {

    public static final String EXTRA_DATE = "extraDate";
    public static final String EXTRA_NAME = "extraName";
    private DatePicker mDatePicker;
    private EditText mEditText;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_campania,null);
        mDatePicker = view.findViewById(R.id.date_picker_campaña);
        mEditText = view.findViewById(R.id.dialog_nombre_campaña);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.datos_campaña)
                .setIcon(R.drawable.campana)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        String nombreCampaña = mEditText.getText().toString();
                        sendResult(Activity.RESULT_OK, date, nombreCampaña);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }
    //Se envia el resultado con un 1, para tomar el caso del activity Result de Crime Fragment, y eliminar el crimen, sin ingresar a otras opciones futuras
    private void sendResult(int resultCode, Date date, String nombreCampaña){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_NAME, nombreCampaña);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


}

