package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class Esterilizacion {
    private UUID mIdEsterilizacion, mIdCampaña, mIdGato;
    private Date mFechaEsterilizacion;
    private int mPrecio, mAnticipo;
    private boolean mFaja;

    public Esterilizacion() {
        this.mIdEsterilizacion = UUID.randomUUID();
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
}
