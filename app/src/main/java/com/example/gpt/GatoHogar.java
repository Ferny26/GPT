package com.example.gpt;

import java.util.UUID;

public class GatoHogar {
    private UUID mGatoHogarId, mPersonaId, mGatoId;

    public GatoHogar(){
        this(UUID.randomUUID());
    }

    public GatoHogar (UUID id){
        this.mGatoHogarId = id;
    }

    public UUID getmGatoHogarId() {
        return mGatoHogarId;
    }

    public UUID getmPersonaId() {
        return mPersonaId;
    }

    public UUID getmGatoId() {
        return mGatoId;
    }

    public void setmPersonaId(UUID mPersonaId) {
        this.mPersonaId = mPersonaId;
    }

    public void setmGatoId(UUID mGatoId) {
        this.mGatoId = mGatoId;
    }
}
