package com.example.gpt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CapitalFragment extends Fragment {

    private CapitalAdapter mAdapter;
    private ImageView mMainImageView;
    private RecyclerView mCapitalRecyclerView;
    private static final int REQUEST_CREATE = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private Spinner mTipoSpinner;
    private Button mCapitalButton;
    private List<Ingreso> recursos;
    private String [] mTipoList = {"Ingresos", "Gastos"};
    private String query;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.universal_list_activity,container,false);
        mMainImageView= v.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.coins);
        mCapitalRecyclerView = v.findViewById(R.id.recyclerView);
        mTipoSpinner = v.findViewById(R.id.estatus_adopcion);
        mCapitalButton = v.findViewById(R.id.estadisticas);
        getActivity().setTitle("Recursos");
        mCapitalButton.setText("Calcular Capital");
        mCapitalButton.setVisibility(View.VISIBLE);
        mTipoSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mTipoList);
        mTipoSpinner.setAdapter(mAdapter);
        mCapitalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();


        mTipoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCapitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                FragmentManager manager = getFragmentManager();
                CapitalDialog dialog = new CapitalDialog();
                dialog.setArguments(arguments);
                dialog.show(manager,DIALOG_CREATE);
            }
        });

        return v;
    }

    private void updateList(){
        if (mTipoSpinner.getSelectedItemId()==0){
            query = "SELECT * FROM ingresos";
        }else{
            query ="SELECT * FROM gastos";
        }
        recursos = IngresoBank.get(getActivity()).getmIngresos(query);
        updateUI();

    }
    @Override
    public void onResume() {
       updateList();
        super.onResume();
    }

    private void updateUI (){
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new CapitalAdapter(recursos);
            mCapitalRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmRecursos(recursos);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mCapitalRecyclerView.setAdapter(mAdapter);
        }
    }

    private class CapitalHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mMotivoTextView, mCantidadTextView, mFechaTextView;
        private Ingreso mRecurso;

        @Override
        public void onClick(View v) {
            Bundle arguments = new Bundle();
            FragmentManager manager = getFragmentManager();
            CrearRecursosDialog dialog = new CrearRecursosDialog();
            arguments.putBoolean("NUEVA_INSTANCIA", false);
            arguments.putSerializable("RECURSO_ID",mRecurso.getmIdIngreso());
            if (mTipoSpinner.getSelectedItemId()==0){
                arguments.putBoolean("TYPE",true);
            }else {
                arguments.putBoolean("TYPE",false);
            }
            dialog.setArguments(arguments);
            dialog.setTargetFragment(CapitalFragment.this, REQUEST_CREATE);
            dialog.show(manager,DIALOG_CREATE);
        }


        public CapitalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recurso_list, parent, false));
            mMotivoTextView = itemView.findViewById(R.id.motivo);
            mCantidadTextView = itemView.findViewById(R.id.cantidad);
            mFechaTextView = itemView.findViewById(R.id.fecha);
            itemView.setOnClickListener(this);
        }


        public void bind (Ingreso recurso){
            mRecurso = recurso;
            mMotivoTextView.setText(mRecurso.getMotivo());
            mCantidadTextView.setText(mRecurso.getmCantidad()+ "");
            mFechaTextView.setText(mRecurso.getDateFormat());
            if (mRecurso.ismAutomatico()){
                itemView.setEnabled(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateList();

    }

    private class CapitalAdapter extends RecyclerView.Adapter<CapitalFragment.CapitalHolder>{
        private List<Ingreso> mRecursos;
        public CapitalAdapter (List<Ingreso> recursos){
            mRecursos = recursos;
        }

        @NonNull
        @Override
        public CapitalFragment.CapitalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CapitalFragment.CapitalHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CapitalFragment.CapitalHolder holder, int position) {
            //Toma los datos del crimen en la posicion indicada y los envia
            Ingreso recurso = mRecursos.get(position);
            holder.bind(recurso);
        }
        @Override
        public int getItemCount() {
            return mRecursos.size();
        }

        public void setmRecursos(List<Ingreso> recursos ){
            mRecursos = recursos;

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recursos_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.añadir:
                Bundle arguments = new Bundle();
                FragmentManager manager = getFragmentManager();
                CrearRecursosDialog dialog = new CrearRecursosDialog();
                arguments.putBoolean("NUEVA_INSTANCIA", true);
               if (mTipoSpinner.getSelectedItemId()==0){
                   arguments.putBoolean("TYPE",true);
               }else {
                   arguments.putBoolean("TYPE",false);
               }
                dialog.setArguments(arguments);
                dialog.setTargetFragment(CapitalFragment.this, REQUEST_CREATE);
                dialog.show(manager,DIALOG_CREATE);
                return true;

            case R.id.borrar_recursos:
                final AlertDialog.Builder mDeleteDialog = new AlertDialog.Builder(getActivity());
                mDeleteDialog.setTitle("Limpiar Registros")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage("Se borraran todos los registros")
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        IngresoBank.get(getActivity()).deleteIngresos("DELETE FROM ingresos");
                                        IngresoBank.get(getActivity()).deleteIngresos("DELETE FROM gastos");
                                        updateList();
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
