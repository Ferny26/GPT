package com.example.gpt;

import java.util.Date;

public class Campaña {
    private String nombreCampaña;
    private Date fechaCampaña;

    public Campaña(String nombreCampaña, Date fechaCampaña) {
        this.nombreCampaña = nombreCampaña;
        this.fechaCampaña = fechaCampaña;
    }

    public String getNombreCampaña() {
        return nombreCampaña;
    }

    public void setNombreCampaña(String nombreCampaña) {
        this.nombreCampaña = nombreCampaña;
    }

    public Date getFechaCampaña() {
        return fechaCampaña;
    }

    public void setFechaCampaña(Date fechaCampaña) {
        this.fechaCampaña = fechaCampaña;
    }
}
