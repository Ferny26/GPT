package com.example.gpt;

import java.util.UUID;

public class MaterialCampaña {
    private UUID mCampañaId, mMaterialId;
    private int mCantidadGastada;

    public MaterialCampaña (UUID id){
        this.mCampañaId = id;
    }

    public UUID getmMaterialId() {
        return mMaterialId;
    }

    public int getmCantidadGastada() {
        return mCantidadGastada;
    }

    public void setmMaterialId(UUID mMaterialId) {
        this.mMaterialId = mMaterialId;
    }

    public void setmCantidadGastada(int mCantidadGastada) {
        this.mCantidadGastada = mCantidadGastada;
    }

    public UUID getmCampañaId() {
        return mCampañaId;
    }
}
