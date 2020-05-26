package com.example.gpt;

import java.util.UUID;

public class GatoHogar {
    private UUID mPersonaId, mGatoId;

    public GatoHogar (UUID id){
        this.mGatoId = id;
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
