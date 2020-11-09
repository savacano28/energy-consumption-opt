package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Dataset comprising date, PPV, conso, prixAchat, prixVente for a specific controlling request
 */
@Entity
@Table(name = "battery_management_instruction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "batterymanagementinstruction")
public class BatteryManagementInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "instant")
    private ZonedDateTime instant;

    @Column(name = "p_prod_global")
    private Double pProdGlobal;

    @Column(name = "delta_prod_global")
    private Double deltaProdGlobal;

    @Column(name = "lambda_prod_global")
    private Double lambdaProdGlobal;

    @Column(name = "p_conso_global")
    private Double pConsoGlobal;

    @Column(name = "delta_conso_global")
    private Double deltaConsoGlobal;

    @Column(name = "lambda_conso_global")
    private Double lambdaConsoGlobal;

    @Column(name = "p_bat")
    private Double pBat;

    @Column(name = "soc")
    private Double soc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "battery_management_run_id")
    private BatteryManagementRun batteryManagementRun;

    public BatteryManagementInstruction() {
        this.instant = instant;
        this.pProdGlobal = pProdGlobal;
        this.deltaProdGlobal = deltaProdGlobal;
        this.lambdaProdGlobal = lambdaProdGlobal;
        this.pConsoGlobal = pConsoGlobal;
        this.deltaConsoGlobal = deltaConsoGlobal;
        this.lambdaConsoGlobal = lambdaConsoGlobal;
        this.pBat = pBat;
        this.soc = soc;
    }

    public BatteryManagementInstruction(ZonedDateTime instant, Double pProdGlobal, Double deltaProdGlobal,
                                        Double lambdaProdGlobal, Double pConsoGlobal, Double deltaConsoGlobal,
                                        Double lambdaConsoGlobal, Double pBat, Double soc) {
        this.instant = instant;
        this.pProdGlobal = pProdGlobal;
        this.deltaProdGlobal = deltaProdGlobal;
        this.lambdaProdGlobal = lambdaProdGlobal;
        this.pConsoGlobal = pConsoGlobal;
        this.deltaConsoGlobal = deltaConsoGlobal;
        this.lambdaConsoGlobal = lambdaConsoGlobal;
        this.pBat = pBat;
        this.soc = soc;
    }


// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getDeltaProdGlobal() {
        return deltaProdGlobal;
    }

    public void setDeltaProdGlobal(Double deltaProdGlobal) {
        this.deltaProdGlobal = deltaProdGlobal;
    }

    public Double getLambdaProdGlobal() {
        return lambdaProdGlobal;
    }

    public void setLambdaProdGlobal(Double lambdaProdGlobal) {
        this.lambdaProdGlobal = lambdaProdGlobal;
    }

    public Double getpConsoGlobal() {
        return pConsoGlobal;
    }

    public void setpConsoGlobal(Double pConsoGlobal) {
        this.pConsoGlobal = pConsoGlobal;
    }

    public Double getDeltaConsoGlobal() {
        return deltaConsoGlobal;
    }

    public void setDeltaConsoGlobal(Double deltaConsoGlobal) {
        this.deltaConsoGlobal = deltaConsoGlobal;
    }

    public Double getLambdaConsoGlobal() {
        return lambdaConsoGlobal;
    }

    public void setLambdaConsoGlobal(Double lambdaConsoGlobal) {
        this.lambdaConsoGlobal = lambdaConsoGlobal;
    }

    public Double getpBat() {
        return pBat;
    }

    public void setpBat(Double pBat) {
        this.pBat = pBat;
    }

    public Double getSoc() {
        return soc;
    }

    public void setSoc(Double soc) {
        this.soc = soc;
    }

    public BatteryManagementRun getBatteryManagementRun() {
        return batteryManagementRun;
    }

    public void setBatteryManagementRun(BatteryManagementRun batteryManagementRun) {
        this.batteryManagementRun = batteryManagementRun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryManagementInstruction that = (BatteryManagementInstruction) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(instant, that.instant) &&
            Objects.equals(pProdGlobal, that.pProdGlobal) &&
            Objects.equals(deltaProdGlobal, that.deltaProdGlobal) &&
            Objects.equals(lambdaProdGlobal, that.lambdaProdGlobal) &&
            Objects.equals(pConsoGlobal, that.pConsoGlobal) &&
            Objects.equals(deltaConsoGlobal, that.deltaConsoGlobal) &&
            Objects.equals(lambdaConsoGlobal, that.lambdaConsoGlobal) &&
            Objects.equals(pBat, that.pBat) &&
            Objects.equals(soc, that.soc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instant, pProdGlobal, deltaProdGlobal, lambdaProdGlobal, pConsoGlobal, deltaConsoGlobal, lambdaConsoGlobal, pBat, soc);
    }

    @Override
    public String toString() {
        return "BatteryManagementInstruction{" +
            "id=" + id +
            ", instant=" + instant +
            ", pProdGlobal=" + pProdGlobal +
            ", deltaProdGlobal=" + deltaProdGlobal +
            ", lambdaProdGlobal=" + lambdaProdGlobal +
            ", pConsoGlobal=" + pConsoGlobal +
            ", deltaConsoGlobal=" + deltaConsoGlobal +
            ", lambdaConsoGlobal=" + lambdaConsoGlobal +
            ", pBat=" + pBat +
            ", soc=" + soc +
            '}';
    }
}
