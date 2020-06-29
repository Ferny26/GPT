package com.example.gpt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class CapitalDialog extends DialogFragment {
    private TextView mDineroTotal;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mDataBase = new GPTBaseHelper(mContext).getWritableDatabase();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.capital_dialog, null);

        mDineroTotal = view.findViewById(R.id.dinero_recabado);
        int ingresos = getEstadisticas("SELECT SUM(cantidad) FROM ingresos");
        int gastos = getEstadisticas("SELECT SUM(cantidad) FROM gastos");
        String dinero = Integer.toString(ingresos-gastos);

        mDineroTotal.setText("$"+dinero);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Ganancias!")
                .setIcon(R.drawable.color_coins)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();

    }

    public int getEstadisticas(String query){
        Cursor cursor = mDataBase.rawQuery(query,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }

}
