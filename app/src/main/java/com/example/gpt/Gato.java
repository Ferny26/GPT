package com.example.gpt;

import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

public class Gato {
    private UUID mIdGato;
    private float mPeso;
    private ImageView mFoto;
    private Date mFechaNacimiento;
    private boolean mSexo;
    private String mNombreGato, mCondicionEspecial, mProcedencia;

    public Gato(){
        this(UUID.randomUUID());
    }

    public Gato(UUID id){
        this.mIdGato=id;
    }

    public UUID getmIdGato() {
        return mIdGato;
    }

    public float getmPeso() {
        return mPeso;
    }

    public ImageView getmFoto() {
        return mFoto;
    }

    public Date getmFechaNacimiento() {
        return mFechaNacimiento;
    }

    public boolean ismSexo() {
        return mSexo;
    }

    public String getmCondicionEspecial() {
        return mCondicionEspecial;
    }

    public String getmProcedencia() {
        return mProcedencia;
    }

    public void setmPeso(float mPeso) {
        this.mPeso = mPeso;
    }

    public void setmFoto(ImageView mFoto) {
        this.mFoto = mFoto;
    }

    public void setmFechaNacimiento(Date mFechaNacimiento) {
        this.mFechaNacimiento = mFechaNacimiento;
    }

    public void setmSexo(boolean mSexo) {
        this.mSexo = mSexo;
    }

    public void setmCondicionEspecial(String mCondicionEspecial) {
        this.mCondicionEspecial = mCondicionEspecial;
    }

    public void setmProcedencia(String mProcedencia) {
        this.mProcedencia = mProcedencia;
    }

    public String getmNombreGato() {
        return mNombreGato;
    }

    public void setmNombreGato(String mNombreGato) {
        this.mNombreGato = mNombreGato;
    }
}
