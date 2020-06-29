package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class Pension {
    private UUID mIdPension, mGatoId;
    private int  mTipoPension, mPrecioDia;
    private Date mFechaIngreso;
    private Date mFechaSalida;
    private boolean validacion;


    public Pension(){
        this(UUID.randomUUID());
    }

    public Pension(UUID id){
        this.mIdPension=id;
        this.mFechaIngreso= new Date();
        this.mFechaSalida= new Date();
    }

    public int getmPrecioDia() {
        return mPrecioDia;
    }

    public void setmPrecioDia(int mPrecioDia) {
        this.mPrecioDia = mPrecioDia;
    }


    public Date getmFechaIngreso() {
        return mFechaIngreso;
    }

    public void setmFechaIngreso(Date mFechaIngreso) {
        this.mFechaIngreso = mFechaIngreso;
    }

    public Date getmFechaSalida() {
        return mFechaSalida;
    }

    public void setmFechaSalida(Date mFechaSalida) {
        this.mFechaSalida = mFechaSalida;
    }


    public boolean isValidacion() {
        return validacion;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
    }

    public UUID getmGatoId() {
        return mGatoId;
    }

    public void setmGatoId(UUID mGatoId) {
        this.mGatoId = mGatoId;
    }

    public int getmTipoPension() {
        return mTipoPension;
    }

    public void setmTipoPension(int mTipoPension) {
        this.mTipoPension = mTipoPension;
    }

    public UUID getmIdPension() {
        return mIdPension;
    }
}
