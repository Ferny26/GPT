package com.example.gpt;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Pension {
    private UUID mIdPension, mGatoId;
    private int  mTipoPension, mPrecioDia;
    private Date mFechaIngreso;
    private Date mFechaSalida;
    private boolean validacion, mPagada=false;
    private CharSequence dateFormat;


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

    public CharSequence getDateFormat(Date fecha) {
        dateFormat =android.text.format.DateFormat.format("dd MMMM yyyy", fecha);
        return dateFormat;
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

    public int gananciaPension(){
        Date fechaIngreso = new GregorianCalendar(mFechaIngreso.getYear(), mFechaIngreso.getMonth(), mFechaIngreso.getDay()).getTime();
        Date fechaSalida = new GregorianCalendar(mFechaSalida.getYear(), mFechaSalida.getMonth(), mFechaSalida.getDay()).getTime();

        long diff = fechaIngreso.getTime() - fechaSalida.getTime() ;
        diff = (diff / (1000 * 60 * 60 * 24));

        diff = diff * mPrecioDia;

        int total = (int) diff;
        return total;
    }

    public boolean ismPagada() {
        return mPagada;
    }

    public void setmPagada(boolean mPagada) {
        this.mPagada = mPagada;
    }
}
