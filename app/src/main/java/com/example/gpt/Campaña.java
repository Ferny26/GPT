package com.example.gpt;

import java.util.Date;
import java.util.UUID;

public class Campaña {
    private UUID mIdCampaña;
    private String mNombreCampaña;
    private Date mFechaCampaña;
    private CharSequence dateFormat;


    public Campaña(){
        this(UUID.randomUUID());
    }

    public Campaña(UUID id) {
        this.mIdCampaña = id;
    }

    public UUID getmIdCampaña() {
        return mIdCampaña;
    }


    public String getmNombreCampaña() {
        return mNombreCampaña;
    }

    public void setmNombreCampaña(String mNombreCampaña) {
        this.mNombreCampaña = mNombreCampaña;
    }

    public CharSequence getDateFormat() {
        dateFormat =android.text.format.DateFormat.format("dd MMMM yyyy", mFechaCampaña);
        return dateFormat;
    }

    public Date getmFechaCampaña() {
        return mFechaCampaña;
    }

    public void setmFechaCampaña(Date mFechaCampaña) {
        this.mFechaCampaña = mFechaCampaña;
    }
}
