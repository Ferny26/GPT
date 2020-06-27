package com.example.gpt;

import android.widget.ImageView;

import java.util.UUID;

public class Material {
    private UUID mMaterialId;
    private String mNombre, mPresentacion;
    private int mTipoInventario, mCategoria, mCantidad;

    public Material (){ this(UUID.randomUUID());}

    public Material (UUID id){
        this.mMaterialId = id;
    }

    public String getmNombre() {
        return mNombre;
    }

    public String getmPresentacion() {
        return mPresentacion;
    }

    public int getmTipoInventario() {
        return mTipoInventario;
    }

    public int getmCategoria() {
        return mCategoria;
    }

    public int getmCantidad() {
        return mCantidad;
    }

    public UUID getmMaterialId() {
        return mMaterialId;
    }


    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public void setmPresentacion(String mPresentacion) {
        this.mPresentacion = mPresentacion;
    }

    public void setmTipoInventario(int mTipoInventario) {
        this.mTipoInventario = mTipoInventario;
    }

    public void setmCategoria(int mCategoria) {
        this.mCategoria = mCategoria;
    }

    public void setmCantidad(int mCantidad) {
        this.mCantidad = mCantidad;
    }

    public String getmPhotoFile(){
        return "IMG_" + getmMaterialId().toString() + ".jpg";
    }
}
