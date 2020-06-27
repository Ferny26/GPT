package com.example.gpt;

import java.util.UUID;

public class Persona {
    private String mNombre, mApellidoPaterno, mApellidoMaterno, mDomicilio, mCelular, mEmail;
    private UUID mIdPersona;

    public Persona(){
        this(UUID.randomUUID());
    }
    public Persona(UUID id){
        this.mIdPersona=id;
    }

    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public String getmApellidoPaterno() {
        return mApellidoPaterno;
    }

    public void setmApellidoPaterno(String mApellidoPaterno) {
        this.mApellidoPaterno = mApellidoPaterno;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmApellidoMaterno() {
        return mApellidoMaterno;
    }

    public void setmApellidoMaterno(String mApellidoMaterno) {
        this.mApellidoMaterno = mApellidoMaterno;
    }

    public String getmDomicilio() {
        return mDomicilio;
    }

    public void setmDomicilio(String mDomicilio) {
        this.mDomicilio = mDomicilio;
    }

    public String getmCelular() {
        return mCelular;
    }

    public void setmCelular(String mCelular) {
        this.mCelular = mCelular;
    }

    public UUID getmIdPersona() {
        return mIdPersona;
    }


}
