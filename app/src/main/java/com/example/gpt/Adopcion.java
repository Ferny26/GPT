package com.example.gpt;

import java.util.UUID;

public class Adopcion {
    private UUID mAdopcionId;

    public Adopcion(UUID id){
        this.mAdopcionId =id;
    }

    public UUID getmAdopcionId() {
        return mAdopcionId;
    }
}
