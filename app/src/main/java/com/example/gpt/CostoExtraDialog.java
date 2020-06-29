package com.example.gpt;

import android.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.UUID;

public class CostoExtraDialog extends DialogFragment {
    public static final String CAMPAÑA_ID = "campañaId";
    private EditText mDescripcionEditText, mCantidadEditText;
    private CostoExtra mCostoExtra;
    private AlertDialog dialog;
    private UUID costoExtraId;
    boolean nuevaInstancia = true;
}
