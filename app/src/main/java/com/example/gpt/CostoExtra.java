package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class CostoExtra {
    private UUID mCostoExtraId, mPensionId;
    private Date mFechaActual;
    private String mDescripcion;
    private int mCantidad;
    private CharSequence dateFormat;

    public CostoExtra(){
        this(UUID.randomUUID());
    }

    public CostoExtra(UUID id){
        this.mCostoExtraId=id;
        this.mFechaActual= new Date();
    }

    public UUID getmCostoExtraId() {
        return mCostoExtraId;
    }

    public UUID getmPensionId() {
        return mPensionId;
    }

    public void setmPensionId(UUID mPensionId) {
        this.mPensionId = mPensionId;
    }

    public Date getmFechaActual() {
        return mFechaActual;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }

    public void setmFechaActual(Date mFechaActual) {
        this.mFechaActual = mFechaActual;
    }

    public void setmDescripcion(String mDescripcion) {
        this.mDescripcion = mDescripcion;
    }

    public int getmCantidad() {
        return mCantidad;
    }

    public void setmCantidad(int mCantidad) {
        this.mCantidad = mCantidad;
    }

    public CharSequence getDateFormat() {
        dateFormat =android.text.format.DateFormat.format("dd MMMM yyyy", mFechaActual);
        return dateFormat;
    }

    public String getPhotoFilename(){
        return "IMG_" + getmCostoExtraId().toString() + ".jpg";
    }
}
