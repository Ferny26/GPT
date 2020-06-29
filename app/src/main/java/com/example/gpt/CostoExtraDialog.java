package com.example.gpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CostoExtraDialog extends DialogFragment {
    public static final String CAMPAÑA_ID = "campañaId";
    private EditText mDescripcionEditText, mCantidadEditText;
    private TextView mFechaActual;
    private ImageButton mCameraButton;
    private ImageView mCostoExtraImageView;
    private CostoExtra mCostoExtra;
    private AlertDialog dialog;
    private UUID mCostoExtraId, mPensionId;
    private static final int REQUEST_FOTO = 1;
    boolean nuevaInstancia = true;
    private File mPhotoFile;
    private Uri photoUri;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.costo_extra_dialog,null);
        mDescripcionEditText = view.findViewById(R.id.descripcion);
        mCantidadEditText = view.findViewById(R.id.cantidad_extra);
        mCameraButton = view.findViewById(R.id.cameraButton);
        mFechaActual = view.findViewById(R.id.fechaActual);
        mCostoExtraImageView = view.findViewById(R.id.costoExtraImgen);
        nuevaInstancia = getArguments().getBoolean("NUEVA_INSTANCIA");
        mPensionId = (UUID) getArguments().getSerializable("PENSION_ID");
        if (nuevaInstancia){
           mCostoExtra = new CostoExtra();
            mFechaActual.setText(mCostoExtra.getDateFormat());
        }



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Costo Extra")
                .setIcon(R.drawable.billete)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCostoExtra.setmDescripcion(mDescripcionEditText.getText().toString());
                        mCostoExtra.setmCantidad(Integer.parseInt(mCantidadEditText.getText().toString()));
                        mCostoExtra.setmPensionId(mPensionId);
                        if (nuevaInstancia){
                            CostoExtraStorage.get(getActivity()).addCostoExtra(mCostoExtra, getActivity());
                        }else{
                            CostoExtraStorage.get(getActivity()).updateCostoExtra(mCostoExtra);
                        }
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                    }
                })
                .setNeutralButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CostoExtraStorage.get(getActivity()).deleteCostoExtra(GPTDbSchema.CostoExtraTable.Cols.UUID + "= ?", new String[]{mCostoExtra.getmCostoExtraId().toString()});
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = CostoExtraStorage.get(getActivity()).getPhotoFile(mCostoExtra);
                photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                List<ResolveInfo> cameraActivities = getActivity().
                        getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission("activity.activityInfo", photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //Se llama a la camara para poder obtener su resultado
                startActivityForResult(captureImage, REQUEST_FOTO);
            }
        });

        mDescripcionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog = (AlertDialog) getDialog();
                if(mDescripcionEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        mCantidadEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dialog = (AlertDialog) getDialog();
                if(mDescripcionEditText.getText().toString().equals("") || mCantidadEditText.getText().toString().equals("")){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_FOTO ){
            putImageView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = (AlertDialog) getDialog();
        if(!nuevaInstancia) {
            mCostoExtraId = (UUID) getArguments().getSerializable("COSTOEXTRA_ID");
            mCostoExtra = CostoExtraStorage.get(getActivity()).getmCostoExtra(mCostoExtraId);
            mPhotoFile = CostoExtraStorage.get(getActivity()).getPhotoFile(mCostoExtra);
            photoUri = FileProvider.getUriForFile(getActivity(), "com.example.gpt.FileProvider", mPhotoFile);
            mDescripcionEditText.setText(mCostoExtra.getmDescripcion());
            String cantidad = Integer.toString(mCostoExtra.getmCantidad());
            mCantidadEditText.setText(cantidad);
            mFechaActual.setText(mCostoExtra.getDateFormat());
            putImageView();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
        }else{
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
        }
    }

    private void putImageView() {
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
            mCostoExtraImageView.setImageBitmap(bmRotated);
        } catch (IOException e) {
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
