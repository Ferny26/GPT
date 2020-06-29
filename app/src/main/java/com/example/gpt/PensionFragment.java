package com.example.gpt;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PensionFragment extends Fragment {
    private ImageView mMainImageView;
    private PensionFragment.PensionAdapter mAdapter;
    private PensionStorage mPensionStorage;
    private RecyclerView mPensionesRecyclerView;
    private String query;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.universal_list_activity,container,false);
        mMainImageView= v.findViewById(R.id.main_image_view);
        getActivity().setTitle("Pensiones");
        mMainImageView.setImageResource(R.drawable.adopcion);
        mPensionesRecyclerView = v.findViewById(R.id.recyclerView);
        mPensionesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        query = "SELECT * FROM adopciones INNER JOIN registro_adopciones ON adopciones.gato_id = registro_adopciones.gato_id ";
        //pensiones = AdopcionStorage.get(getActivity()).getmAdopciones(query);
        updateUI();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.agregar_pension,menu);
    }

    private void updateUI (){
        mPensionStorage= PensionStorage.get(getActivity());
        List<Pension> pensiones = mPensionStorage.getmPensiones();
        if (mAdapter == null) {
            //Envia la informacion al adaptador
            mAdapter = new PensionFragment.PensionAdapter(pensiones);
            mPensionesRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setmPensiones(pensiones);
            mAdapter.notifyDataSetChanged();//Actualiza los datos del item
            mPensionesRecyclerView.setAdapter(mAdapter);
        }

    }



    private class PensionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mNombreTextView;
        private ImageView mGatoImageView;
        private Pension mPension;

        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(getActivity(), AdopcionesActivity.class);
            intent.putExtra("TYPE", false);
            intent.putExtra("ADOPCION_ID", mAdopcion.getmAdopcionId());
            startActivity(intent);*/
        }

        public PensionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.esterilizacion_list_fragment, parent, false));
            mNombreTextView = itemView.findViewById(R.id.nombreMaterial);
            mGatoImageView= itemView.findViewById(R.id.material_foto);
            itemView.setOnClickListener(this);
        }

        public void bind (Gato gato, Pension pension){
            mPension = pension;
            mNombreTextView.setText(gato.getmNombreGato());
            putImageView(gato, mGatoImageView);
        }
    }


    private class PensionAdapter extends RecyclerView.Adapter<PensionFragment.PensionHolder>{
        private List<Pension> mPensiones;
        public PensionAdapter (List<Pension> pensiones){
            mPensiones = pensiones;
        }

        @NonNull
        @Override
        public PensionFragment.PensionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PensionFragment.PensionHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PensionFragment.PensionHolder holder, int position) {
            Pension pension = mPensiones.get(position);
            CatLab mCatLab = CatLab.get(getActivity());
            Gato mgato = mCatLab.getmGato(pension.getmGatoId());
            holder.bind(mgato, pension);
        }
        @Override
        public int getItemCount() {
            return mPensiones.size();
        }

        public void setmPensiones(List<Pension> pensiones ){
            mPensiones = pensiones;
        }
    }


    @Override
    public void onResume() {
        updateUI();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregar_pension:
                Intent intent = new Intent(getContext(), PensionActivity.class);
                intent.putExtra("TYPE",true);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void putImageView(Gato mGato, ImageView mGatoImageView ) {
        File mPhotoFile = CatLab.get(getActivity()).getPhotoFile(mGato);
        Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
        Bitmap bitmap;
        try {
            //Recupera la foto segun el uri y la asigna a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            //Asignacion de orientacion correcta para la foto
            ExifInterface exif = null;
            exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            //obtiene la orientacion de la foto
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            //Envia la orientacion y el bitmap como parametros para modificarlos
            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
            //Una vez adecuada la foto, se coloca en el imageView
            mGatoImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
            mGatoImageView.setImageResource(R.drawable.gato_gris);
            e.printStackTrace();
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
