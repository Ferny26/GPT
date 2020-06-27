package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class Ingreso {
    private UUID mIdIngreso;
    private String Motivo;
    private Date mFecha;
    private int mCantidad;
    private boolean mAutomatico;

    public UUID getmIdIngreso() {
        return mIdIngreso;
    }

    public Ingreso(){
        this(UUID.randomUUID());
    }

    public Ingreso(UUID id) {
        this.mIdIngreso= id;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String motivo) {
        Motivo = motivo;
    }

    public Date getmFecha() {
        return mFecha;
    }

    public void setmFecha(Date mFecha) {
        this.mFecha = mFecha;
    }

    public int getmCantidad() {
        return mCantidad;
    }

    public void setmCantidad(int mCantidad) {
        this.mCantidad = mCantidad;
    }

    public boolean ismAutomatico() {
        return mAutomatico;
    }

    public void setmAutomatico(boolean mAutomatico) {
        this.mAutomatico = mAutomatico;
    }
}
