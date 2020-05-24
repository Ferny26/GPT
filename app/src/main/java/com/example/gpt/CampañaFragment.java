package com.example.gpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;


public class CampañaFragment extends Fragment {

    private CampañaAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final int REQUEST_CREATE = 0;
    private static final String DIALOG_CREATE = "DialogCreate";
    private CampañaStorage mCampañaStorage;
    private RecyclerView mCampañaRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.campa_as);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.campanas_fragment,container,false);
        mCampañaRecyclerView = v.findViewById(R.id.recyclerView);
        mCampañaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }


    private void updateUI (){
        mCampañaStorage= CampañaStorage.get(getActivity());
        List<Campaña> campañas = mCampañaStorage.getmCampañas();
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new CampañaAdapter(campañas);
            mCampañaRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmCampañas(campañas);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
        }

        updateSubtitle();

    }


    private class CampañasHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView, mFechaTextView;
        private Campaña mCampaña;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ListCampaña.class);
            intent.putExtra("CAMPAÑA_ID", mCampaña.getmIdCampaña());
            intent.putExtra("TYPE",false);
            startActivity(intent);
        }
        public CampañasHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.campania_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombre_campaña);
            mFechaTextView = itemView.findViewById(R.id.fecha_camapaña);
            itemView.setOnClickListener(this);
        }

        public void bind (Campaña campaña){
            mCampaña= campaña;
            if(mCampaña.getmNombreCampaña() == null){
                mNombreTextView.setText("Sin nombre");
            }else{
                mNombreTextView.setText(campaña.getmNombreCampaña());
            }
            mFechaTextView.setText(campaña.getDateFormat().toString());
        }
    }




    private class CampañaAdapter extends RecyclerView.Adapter<CampañasHolder>{
        private List<Campaña> mCampañas;
        public CampañaAdapter (List<Campaña> campañas){
            mCampañas = campañas;
        }

        @NonNull
        @Override
        public CampañasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CampañasHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CampañasHolder holder, int position) {
            //Toma los datos del crimen en la posicion indicada y los envia
            Campaña campaña = mCampañas.get(position);
            holder.bind(campaña);
        }
        @Override
        public int getItemCount() {
            return mCampañas.size();
        }

        public void setmCampañas(List<Campaña> campañas ){
            mCampañas = campañas;

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    private void updateSubtitle(){
        mCampañaStorage = mCampañaStorage.get(getActivity());
        int campañasCount = mCampañaStorage.getmCampañas().size();
        String subtitle = getString(R.string.subtitle_format, campañasCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.campa_a_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        //Cambia el subtitulo según la opción seleccionada
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.ocultar);
        }else {
            subtitleItem.setTitle(R.string.contar_campa_as);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.añadir:
                FragmentManager manager = getFragmentManager();
                CrearCampañaDialog dialog = new CrearCampañaDialog();
                dialog.setTargetFragment(CampañaFragment.this, REQUEST_CREATE);
                dialog.show(manager,DIALOG_CREATE);
                return true;

            case R.id.inventario_botiquin:
                Intent intent = new Intent(getContext(), ListCampaña.class);
                intent.putExtra("TYPE",true);
                startActivity(intent);
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CREATE) {
            Date fecha = (Date) data.getSerializableExtra(CrearCampañaDialog.EXTRA_DATE);
            String nombreCampaña = (String) data.getSerializableExtra(CrearCampañaDialog.EXTRA_NAME);
            Campaña campaña = new Campaña();
            campaña.setmNombreCampaña(nombreCampaña);
            campaña.setmFechaCampaña(fecha);
            CampañaStorage.get(getActivity()).addCampaña(campaña, getActivity());
            Intent intent = new Intent(getContext(), ListCampaña.class);
            intent.putExtra("CAMPAÑA_ID", campaña.getmIdCampaña());
            intent.putExtra("TYPE",false);
            startActivity(intent);
        }
    }
}
