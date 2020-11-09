package fr.ifpen.synergreen.service.dto;


public class BatteryManagementPredictionDTO {

    private String date;
    private Double PPV;
    private Double conso;
    private Double prixAchat;
    private Double prixVente;

    /*getters and setters*/
    public String getDate() {
        return date;
    }

    public void setDate(String instant) {
        this.date = instant;
    }

    public Double getPPV() {
        return PPV;
    }

    public void setPPV(Double PPV) {
        this.PPV = PPV;
    }

    public Double getConso() {
        return conso;
    }

    public void setConso(Double conso) {
        this.conso = conso;
    }

    public Double getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(Double prixVente) {
        this.prixVente = prixVente;
    }

    @Override
    public String toString() {
        return "BatteryManagementInstructionDTO{" +
            "date=" + date +
            ", PPV=" + PPV +
            ", conso=" + conso +
            ", prixAchat=" + prixAchat +
            ", prixVente=" + prixVente +
            '}';
    }
}
