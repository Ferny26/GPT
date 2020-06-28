package com.example.gpt;

import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

public class Gato {
    private UUID mIdGato;
    private Date mFechaNacimiento;
    private int  mProcedencia;
    private String mSexo;
    private String mNombreGato, mCondicionEspecial, mPeso;
    private boolean validacion;

    public boolean isValidacion() {
        return validacion;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
    }

    public Gato(){
        this(UUID.randomUUID());
    }

    public Gato(UUID id){
        this.mIdGato=id;
        this.mFechaNacimiento= new Date(); 
    }

    public UUID getmIdGato() {
        return mIdGato;
    }

    public String getmPeso() {
        return mPeso;
    }


    public Date getmFechaNacimiento() {
        return mFechaNacimiento;
    }

    public String ismSexo() {
        return mSexo;
    }

    public String getmCondicionEspecial() {
        return mCondicionEspecial;
    }

    public int getmProcedencia() {
        return mProcedencia;
    }

    public void setmPeso(String mPeso) {
        this.mPeso = mPeso;
    }



    public void setmFechaNacimiento(Date mFechaNacimiento) {
        this.mFechaNacimiento = mFechaNacimiento;
    }

    public void setmSexo(String mSexo) {
        this.mSexo = mSexo;
    }

    public void setmCondicionEspecial(String mCondicionEspecial) {
        this.mCondicionEspecial = mCondicionEspecial;
    }

    public void setmProcedencia(int mProcedencia) {
        this.mProcedencia = mProcedencia;
    }

    public String getmNombreGato() {
        return mNombreGato;
    }

    public void setmNombreGato(String mNombreGato) {
        this.mNombreGato = mNombreGato;
    }

    public String getPhotoFilename(){
        return "IMG_" + getmIdGato().toString() + ".jpg";
    }

}
