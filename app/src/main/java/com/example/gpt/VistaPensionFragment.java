package com.example.gpt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class VistaPensionFragment extends Fragment {

    private Gato mGato;
    private Button mPagadoButton;
    private TextView mFechaIngreso, mFechaSalida, mNombreGato;
    private Pension mPension;
    private UUID pensionId;
    private ImageView mGatoImageView;
    private CostoExtraAdapter mAdapter;
    private RecyclerView mCostoExtraRecyclerView;
    private Button mCostoExtraButton;
    private File mPhotoFile;
    private Uri mPhotoUri;
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

        mPagadoButton = view.findViewById(R.id.pagado);
        mFechaSalida = view.findViewById(R.id.fecha_ingreso);
        mGatoImageView = view.findViewById(R.id.imagen_pension_gato);
        mFechaIngreso = view.findViewById(R.id.fecha_salida);
        mNombreGato = view.findViewById(R.id.nombre_Gato);

        mPension = PensionStorage.get(getActivity()).getmPension(pensionId);

        mFechaIngreso.setText(mPension.getDateFormat(mPension.getmFechaIngreso()));
        mFechaSalida.setText(mPension.getDateFormat(mPension.getmFechaSalida()));

        mGato = CatLab.get(getActivity()).getmGato(mPension.getmGatoId());

        mNombreGato.setText(mGato.getmNombreGato());
        mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        putImageView(mGatoImageView);

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

    @Override
    public void onResume() {
        mPension = PensionStorage.get(getActivity()).getmPension(pensionId);
        mGato = CatLab.get(getActivity()).getmGato(mPension.getmGatoId());
        mFechaIngreso.setText(mPension.getDateFormat(mPension.getmFechaIngreso()));
        mFechaSalida.setText(mPension.getDateFormat(mPension.getmFechaSalida()));
        mNombreGato.setText(mGato.getmNombreGato());
        mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        mPhotoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        putImageView(mGatoImageView);
        super.onResume();
    }

    @Override
    public void onPause() {
        Ingreso mIngreso = IngresoBank.get(getActivity()).getmIngreso(mPension.getmIdPension());
        if (IngresoBank.get(getActivity()).getmIngreso(mPension.getmIdPension())!=null) {
            if(mPension.ismPagada()){
                String query = "SELECT SUM(cantidad) FROM costo_extra WHERE pension_id='" + pensionId + "'";
                int costosExtra = CostoExtraStorage.get(getActivity()).getmPrecioTotal(query);
                mIngreso.setmCantidad(mPension.gananciaPension() + costosExtra);
                IngresoBank.get(getActivity()).updateIngreso(mIngreso,GPTDbSchema.IngresoTable.NAME,GPTDbSchema.IngresoTable.Cols.UUID);
            }else{
                IngresoBank.get(getActivity()).deleteIngreso(GPTDbSchema.IngresoTable.Cols.UUID + "= ?", new String[]{mIngreso.getmIdIngreso().toString()}, GPTDbSchema.IngresoTable.NAME);
            }
        }else{
            if(mPension.ismPagada()){
                CrearIngreso();
            }
        }
        super.onPause();
    }

    public void CrearIngreso(){
            Ingreso mIngreso = new Ingreso(mPension.getmIdPension());
            mIngreso.setmAutomatico(true);
            mIngreso.setMotivo("Pension");
            String query = "SELECT SUM(cantidad) FROM costo_extra WHERE pension_id='" + pensionId + "'";
            int costosExtra = CostoExtraStorage.get(getActivity()).getmPrecioTotal(query);
            mIngreso.setmCantidad(mPension.gananciaPension() + costosExtra);
            mIngreso.setmFecha(mPension.getmFechaSalida());
            IngresoBank.get(getActivity()).addIngreso(mIngreso, getActivity(),GPTDbSchema.IngresoTable.NAME );
    }

    private void updateUI (){
        costos = CostoExtraStorage.get(getActivity()).getmCostosExtra(pensionId);
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

    private void putImageView(ImageView mImageView) {
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPhotoUri);
            //Asignacion de orientacion correcta para la foto
            ExifInterface exif = null;
            exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            //obtiene la orientacion de la foto
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //Envia la orientacion y el bitmap como parametros para modificarlos
            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
            //Una vez adecuada la foto, se coloca en el imageView
            mImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
            e.printStackTrace();
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

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        //Verifica la orientacion de la foto y asigna los parametros necesarios de escala y rotacion
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            //Crea un nuevo bitmap con los parametros correctos de orientacion de la foto y lo regresa
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}

