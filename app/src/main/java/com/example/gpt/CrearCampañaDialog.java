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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class CrearCampañaDialog extends DialogFragment {

    public static final String EXTRA_DATE = "extraDate";
    public static final String EXTRA_NAME = "extraName";
    public static final String CAMPAÑA_ID = "campañaId";
    private DatePicker mDatePicker;
    private EditText mEditText;
    private CampañaStorage mCampañaStorage;
    private Campaña mCampaña;
    AlertDialog dialog;
    boolean nuevaInstancia = false;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_campania,null);
        mDatePicker = view.findViewById(R.id.date_picker_campaña);
        mEditText = view.findViewById(R.id.dialog_nombre_campaña);
        nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
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
                });



        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog = (AlertDialog) getDialog();
                if(mEditText.getText().toString().equals("")){

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

        return builder.create();
    }

    private void sendResult(int resultCode, Date date, String nombreCampaña){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_NAME, nombreCampaña);
        if(nuevaInstancia){
            mCampaña.setmFechaCampaña(date);
            mCampaña.setmNombreCampaña(nombreCampaña);
            CampañaStorage.get(getActivity()).updateCampaña(mCampaña);
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(nuevaInstancia) {
            UUID campañaId = (UUID) getArguments().getSerializable("CAMPAÑA_ID");
            mCampaña = mCampañaStorage.get(getActivity()).getCampaña(campañaId);
            mEditText.setText(mCampaña.getmNombreCampaña());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCampaña.getmFechaCampaña());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year, month, day,null);
            dialog = (AlertDialog) getDialog();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

}

