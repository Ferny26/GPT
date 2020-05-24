package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class Esterilizacion {
    private UUID mIdEsterilizacion, mIdCampaña, mIdGato;
    private Date mFechaEsterilizacion;
    private int mPrecio, mAnticipo;
    private boolean mFaja, mPagado;

    public Esterilizacion(){
        this(UUID.randomUUID());
    }
    public Esterilizacion(UUID id) {
        this.mIdEsterilizacion = id;
        this.mFechaEsterilizacion = new Date();
    }

    public UUID getmIdEsterilizacion() {
        return mIdEsterilizacion;
    }

    public Date getmFechaEsterilizacion() {
        return mFechaEsterilizacion;
    }

    public int getmPrecio() {
        return mPrecio;
    }

    public int getmAnticipo() {
        return mAnticipo;
    }

    public boolean ismFaja() {
        return mFaja;
    }

    public void setmFechaEsterilizacion(Date mFechaEsterilizacion) {
        this.mFechaEsterilizacion = mFechaEsterilizacion;
    }

    public void setmPrecio(int mPrecio) {
        this.mPrecio = mPrecio;
    }

    public void setmAnticipo(int mAnticipo) {
        this.mAnticipo = mAnticipo;
    }

    public void setmFaja(boolean mFaja) {
        this.mFaja = mFaja;
    }

    public UUID getmIdCampaña() {
        return mIdCampaña;
    }

    public void setmIdCampaña(UUID mIdCampaña) {
        this.mIdCampaña = mIdCampaña;
    }

    public UUID getmIdGato() {
        return mIdGato;
    }

    public boolean ismPagado() {
        return mPagado;
    }

    public void setmPagado(boolean mPagado) {
        this.mPagado = mPagado;
    }

    public void setmIdGato(UUID mIdGato) {
        this.mIdGato = mIdGato;
    }
}
