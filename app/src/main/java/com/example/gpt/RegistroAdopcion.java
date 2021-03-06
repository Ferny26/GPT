package com.example.gpt;

import java.util.UUID;

public class RegistroAdopcion {
    private UUID mRegistroAdopcionId, mGatoId, mAdoptanteId;

    public RegistroAdopcion(){
        this(UUID.randomUUID());
    }

    public RegistroAdopcion(UUID id){
        this.mRegistroAdopcionId =id;
    }

    public UUID getmRegistroAdopcionId() {
        return mRegistroAdopcionId;
    }

    public UUID getmGatoId() {
        return mGatoId;
    }

    public void setmGatoId(UUID mGatoId) {
        this.mGatoId = mGatoId;
    }

    public UUID getmAdoptanteId() {
        return mAdoptanteId;
    }

    public void setmAdoptanteId(UUID mAdoptanteId) {
        this.mAdoptanteId = mAdoptanteId;
    }


    public String getPhotoFilename(){
        return "IMG_" + getmRegistroAdopcionId().toString() + ".jpg";
    }
}
