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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class VistaPensionFragment extends Fragment {

    private UUID pensionId;
    private CostoExtraAdapter mAdapter;
    private RecyclerView mCostoExtraRecyclerView;
    private Button mCostoExtraButton;
    private static final int REQUEST_CREATE = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private List<CostoExtra> costos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        pensionId = (UUID) getArguments().getSerializable("PENSION_ID");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vista_pension_fragment,container,false);
        mCostoExtraButton = view.findViewById(R.id.costo_extra);
        mCostoExtraRecyclerView = view.findViewById(R.id.costo_extra_list);
        mCostoExtraRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle("Pensiones");
        setHasOptionsMenu(true);

        mCostoExtraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                FragmentManager manager = getFragmentManager();
                CostoExtraDialog dialog = new CostoExtraDialog();
                arguments.putBoolean("NUEVA_INSTANCIA", true);
                arguments.putSerializable("PENSION_ID",pensionId);
                dialog.setArguments(arguments);
                dialog.setTargetFragment(VistaPensionFragment.this, REQUEST_CREATE);
                dialog.show(manager,DIALOG_CREATE);
            }
        });
        updateUI();
        return view;
    }

    private void updateUI (){
        costos = CostoExtraStorage.get(getActivity()).getmCostosExtra();
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new CostoExtraAdapter(costos);
            mCostoExtraRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmCostosExtra(costos);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mCostoExtraRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
    }

    private class CostoExtraHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mCostoTextView, mFechaTextView;
        private CostoExtra mCostoExtra;

        @Override
        public void onClick(View v) {
            Bundle arguments = new Bundle();
            FragmentManager manager = getFragmentManager();
            CostoExtraDialog dialog = new CostoExtraDialog();
            arguments.putBoolean("NUEVA_INSTANCIA", false);
            arguments.putSerializable("PENSION_ID",pensionId);
            arguments.putSerializable("COSTOEXTRA_ID",mCostoExtra.getmCostoExtraId());
            dialog.setArguments(arguments);
            dialog.setTargetFragment(VistaPensionFragment.this, REQUEST_CREATE);
            dialog.show(manager,DIALOG_CREATE);
        }

        public CostoExtraHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.campania_list_fragment, parent, false));
            mCostoTextView = itemView.findViewById(R.id.nombre_campaña);
            mFechaTextView = itemView.findViewById(R.id.fecha_camapaña);
            itemView.setOnClickListener(this);
        }

        public void bind (CostoExtra costoExtra){
            mCostoExtra = costoExtra;
            mCostoTextView.setText("$" + mCostoExtra.getmCantidad());
            mFechaTextView.setText(mCostoExtra.getDateFormat());
        }
    }


    private class CostoExtraAdapter extends RecyclerView.Adapter<VistaPensionFragment.CostoExtraHolder>{
        private List<CostoExtra> mCostosExtra;
        public CostoExtraAdapter (List<CostoExtra> costosExtra){
            mCostosExtra = costosExtra;
        }

        @NonNull
        @Override
        public VistaPensionFragment.CostoExtraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new VistaPensionFragment.CostoExtraHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull VistaPensionFragment.CostoExtraHolder holder, int position) {
            CostoExtra costoExtra = mCostosExtra.get(position);
            holder.bind(costoExtra);
        }
        @Override
        public int getItemCount() {
            return mCostosExtra.size();
        }

        public void setmCostosExtra(List<CostoExtra> costosExtra ){
            mCostosExtra = costosExtra;
        }
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

