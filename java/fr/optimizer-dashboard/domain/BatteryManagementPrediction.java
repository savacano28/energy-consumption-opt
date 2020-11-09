package fr.ifpen.synergreen.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Dataset comprising date, PPV, conso, prixAchat, prixVente for a specific controlling request
 */
public class BatteryManagementPrediction implements Serializable {

    private static final long serialVersionUID = 1L;

    private ZonedDateTime instant;
    private Double pProdGlobal;
    private Double pConsoGlobal;
    private Double prixAchat;
    private Double prixVente;

    public BatteryManagementPrediction() {
        this.instant = instant;
        this.pProdGlobal = pProdGlobal;
        this.pConsoGlobal = pConsoGlobal;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
    }


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public ZonedDateTime getInstant() {
        return instant;
    }

    public void setInstant(ZonedDateTime instant) {
        this.instant = instant;
    }

    public Double getpProdGlobal() {
        return pProdGlobal;
    }

    public void setpProdGlobal(Double pProdGlobal) {
        this.pProdGlobal = pProdGlobal;
    }

    public Double getpConsoGlobal() {
        return pConsoGlobal;
    }

    public void setpConsoGlobal(Double pConsoGlobal) {
        this.pConsoGlobal = pConsoGlobal;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryManagementPrediction that = (BatteryManagementPrediction) o;
        return Objects.equals(instant, that.instant) &&
            Objects.equals(pProdGlobal, that.pProdGlobal) &&
            Objects.equals(pConsoGlobal, that.pConsoGlobal) &&
            Objects.equals(prixAchat, that.prixAchat) &&
            Objects.equals(prixVente, that.prixVente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant, pProdGlobal, pConsoGlobal, prixAchat, prixVente);
    }

    @Override
    public String toString() {
        return "BatteryManagementPrediction{" +
            "instant=" + instant +
            ", pProdGlobal=" + pProdGlobal +
            ", pConsoGlobal=" + pConsoGlobal +
            ", prixAchat=" + prixAchat +
            ", prixVente=" + prixVente +
            '}';
    }
}
