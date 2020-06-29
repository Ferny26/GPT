package com.example.gpt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.UUID;

public class VistaPensionFragment extends Fragment {

    UUID pensionId;
    Gato mGato;
    Button mPagadoButton;
    TextView mFechaIngreso, mFechaSalida, mNombreGato;
    Pension mPension;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        pensionId = (UUID) getArguments().getSerializable("PENSION_ID");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vista_pension_fragment,container,false);

        mPagadoButton = view.findViewById(R.id.pagado);
        mFechaSalida = view.findViewById(R.id.fecha_ingreso);
        mFechaIngreso = view.findViewById(R.id.fecha_salida);
        mNombreGato = view.findViewById(R.id.nombre_Gato);

        mPension = PensionStorage.get(getActivity()).getmPension(pensionId);

        mFechaIngreso.setText(mPension.getDateFormat(mPension.getmFechaIngreso()));
        mFechaSalida.setText(mPension.getDateFormat(mPension.getmFechaSalida()));

        mGato = CatLab.get(getActivity()).getmGato(mPension.getmGatoId());

        mNombreGato.setText(mGato.getmNombreGato());

        getActivity().setTitle("Pensiones");
        setHasOptionsMenu(true);

        if(mPension.ismPagada()){
            mPagadoButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }else{
            mPagadoButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }


        mPagadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPension.ismPagada()){
                    mPension.setmPagada(true);
                    mPagadoButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    PensionStorage.get(getContext()).updatePension(mPension);
                }else{
                    mPension.setmPagada(false);
                    mPagadoButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    PensionStorage.get(getContext()).updatePension(mPension);
                }
            }
        });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_adopcion,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editar_gato:
                Intent intent = new Intent(getContext(), PensionActivity.class);
                intent.putExtra("PENSION_ID", pensionId);
                intent.putExtra("NUEVA_INSTANCIA", false);
                startActivity(intent);
                return true;
            case R.id.borrar_adopcion:
                final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(getActivity());
                mDeleteDialog.setTitle("Borrar Pension")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Estas segura de borrar la pensión? el ingreso se mantendrá si está pagada")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PensionStorage.get(getActivity()).deletePension(GPTDbSchema.PensionTable.Cols.UUID + "= ?", new String[] {pensionId.toString()});
                                        getActivity().finish();
                                    }
                                })
                        //Cancela la accion de delete
                        .setNegativeButton("cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

