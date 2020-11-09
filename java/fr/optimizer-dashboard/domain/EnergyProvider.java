package fr.ifpen.synergreen.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Month;
import java.util.*;

/**
 * A EnergyProvider.
 */
@Entity
@Table(name = "energy_provider")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "energyprovider")
public class EnergyProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "purchase_rate")
    private Double purchaseRate;  //provider achat à EMS

    @Column(name = "purchase_auto_conso_rate")
    private Double purchaseAutoConsoRate;

    @Column(name = "sell_low_hours_low_season")
    private Double sellLowHoursLowSeason;

    @Column(name = "sell_low_hours_high_season")
    private Double sellLowHoursHighSeason;

    @Column(name = "sell_peak_hours_low_season")
    private Double sellPeakHoursLowSeason;

    @Column(name = "sell_peak_hours_high_season")
    private Double sellPeakHoursHighSeason;

    @Column(name = "sell_high_hours_low_season")
    private Double sellHighHoursLowSeason;

    @Column(name = "sell_high_hours_high_season")
    private Double sellHighHoursHighSeason;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "months_off_peak", joinColumns = @JoinColumn(name = "energy_provider_id", referencedColumnName = "id"))
    @Column(name = "month", length = 250, nullable = false)
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private Set<Month> monthsOffPeaks = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "months_high_season", joinColumns = @JoinColumn(name = "energy_provider_id", referencedColumnName = "id"))
    @Column(name = "month", length = 250, nullable = false)
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private Set<Month> monthsHighSeasons = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "high_hours", joinColumns = @JoinColumn(name = "energy_provider_id", referencedColumnName = "id"))
    @Column(name = "hour", length = 250, nullable = false)
    @BatchSize(size = 10)
    private Set<Integer> highHours = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "low_hours", joinColumns = @JoinColumn(name = "energy_provider_id", referencedColumnName = "id"))
    @Column(name = "hour", length = 250, nullable = false)
    @BatchSize(size = 10)
    private Set<Integer> lowHours = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "peak_hours", joinColumns = @JoinColumn(name = "energy_provider_id", referencedColumnName = "id"))
    @Column(name = "hour", length = 250, nullable = false)
    @BatchSize(size = 10)
    private Set<Integer> peakHours = new HashSet<>();

    @OneToMany(mappedBy = "energyProvider")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 5)
    private Set<FluxTopology> fluxTopologies = new HashSet<>();

    @OneToMany(mappedBy = "energyProvider")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 5)
    private Set<FluxNode> fluxNodes = new HashSet<>();

    @Column(name = "ps")
    private Double ps;

    @Column(name = "delta")
    private Double delta;

    @Transient
    private Map<Integer, Double> sellPriceMatrix = new LinkedHashMap<>();

    public void buildMatrixPrices() {
        sellPriceMatrix.put(0, sellHighHoursLowSeason);
        sellPriceMatrix.put(1, sellLowHoursLowSeason);
        sellPriceMatrix.put(2, sellPeakHoursLowSeason);
        sellPriceMatrix.put(3, sellHighHoursHighSeason);
        sellPriceMatrix.put(4, sellLowHoursHighSeason);
        sellPriceMatrix.put(5, sellPeakHoursHighSeason);
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnergyProvider name(String name) {
        this.name = name;
        return this;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public EnergyProvider purchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
        return this;
    }

    public Double getPurchaseAutoConsoRate() {
        return purchaseAutoConsoRate;
    }

    public void setPurchaseAutoConsoRate(Double purchaseAutoConsoRate) {
        this.purchaseAutoConsoRate = purchaseAutoConsoRate;
    }

    public EnergyProvider purchaseAutoConsoRate(Double purchaseAutoConsoRate) {
        this.purchaseAutoConsoRate = purchaseAutoConsoRate;
        return this;
    }

    public Set<Month> getMonthsHighSeasons() {
        return monthsHighSeasons;
    }

    public void setMonthsHighSeasons(Set<Month> monthsHighSeasons) {
        this.monthsHighSeasons = monthsHighSeasons;
    }

    public EnergyProvider monthsHighSeason(Set<Month> monthsHighSeasons) {
        this.monthsHighSeasons = monthsHighSeasons;
        return this;
    }

    public Set<Month> getMonthsOffPeaks() {
        return monthsOffPeaks;
    }

    public void setMonthsOffPeaks(Set<Month> monthsOffPeaks) {
        this.monthsOffPeaks = monthsOffPeaks;
    }

    public EnergyProvider monthsOffPeaks(Set<Month> monthsOffPeaks) {
        this.monthsOffPeaks = monthsOffPeaks;
        return this;
    }

    public Set<Integer> getHighHours() {
        return highHours;
    }

    public void setHighHours(Set<Integer> highHours) {
        this.highHours = highHours;
    }

    public EnergyProvider highHours(Set<Integer> highHours) {
        this.highHours = highHours;
        return this;
    }

    public Set<Integer> getLowHours() {
        return lowHours;
    }

    public void setLowHours(Set<Integer> lowHours) {
        this.lowHours = lowHours;
    }

    public EnergyProvider lowHours(Set<Integer> lowHours) {
        this.lowHours = lowHours;
        return this;
    }

    public Set<Integer> getPeakHours() {
        return peakHours;
    }

    public void setPeakHours(Set<Integer> peakHours) {
        this.peakHours = peakHours;
    }

    public EnergyProvider peakHours(Set<Integer> peakHours) {
        this.peakHours = peakHours;
        return this;
    }

    public Set<FluxTopology> getFluxTopology() {
        return fluxTopologies;
    }

    public void setFluxTopology(Set<FluxTopology> fluxTopologies) {
        this.fluxTopologies = fluxTopologies;
    }

    public EnergyProvider fluxTopology(Set<FluxTopology> fluxTopologies) {
        this.fluxTopologies = fluxTopologies;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Double getSellLowHoursLowSeason() {
        return sellLowHoursLowSeason;
    }

    public void setSellLowHoursLowSeason(Double sellLowHoursLowSeason) {
        this.sellLowHoursLowSeason = sellLowHoursLowSeason;
        sellPriceMatrix.put(1, sellLowHoursLowSeason);
    }

    public Double getSellLowHoursHighSeason() {
        return sellLowHoursHighSeason;
    }

    public void setSellLowHoursHighSeason(Double sellLowHoursHighSeason) {
        this.sellLowHoursHighSeason = sellLowHoursHighSeason;
        sellPriceMatrix.put(4, sellLowHoursHighSeason);
    }

    public Double getSellPeakHoursLowSeason() {
        return sellPeakHoursLowSeason;
    }

    public void setSellPeakHoursLowSeason(Double sellPeakHoursLowSeason) {
        this.sellPeakHoursLowSeason = sellPeakHoursLowSeason;
        sellPriceMatrix.put(2, sellPeakHoursLowSeason);
    }

    public Double getSellPeakHoursHighSeason() {
        return sellPeakHoursHighSeason;
    }

    public void setSellPeakHoursHighSeason(Double sellPeakHoursHighSeason) {
        this.sellPeakHoursHighSeason = sellPeakHoursHighSeason;
        sellPriceMatrix.put(5, sellPeakHoursHighSeason);
    }

    public Double getSellHighHoursLowSeason() {
        return sellHighHoursLowSeason;
    }

    public void setSellHighHoursLowSeason(Double sellHighHoursLowSeason) {
        this.sellHighHoursLowSeason = sellHighHoursLowSeason;
        sellPriceMatrix.put(0, sellHighHoursLowSeason);
    }

    public Double getSellHighHoursHighSeason() {
        return sellHighHoursHighSeason;
    }

    public void setSellHighHoursHighSeason(Double sellHighHoursHighSeason) {
        this.sellHighHoursHighSeason = sellHighHoursHighSeason;
        sellPriceMatrix.put(3, sellHighHoursHighSeason);
    }

    public Map<Integer, Double> getSellPriceMatrix() {
        return sellPriceMatrix;
    }

    public void setSellPriceMatrix(Map<Integer, Double> sellPriceMatrix) {
        this.sellPriceMatrix = sellPriceMatrix;
    }


    public Double getPs() {
        return ps;
    }

    public void setPs(Double ps) {
        this.ps = ps;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Set<FluxNode> getFluxNode() {
        return fluxNodes;
    }

    public void setFluxNode(Set<FluxNode> fluxNodes) {
        this.fluxNodes = fluxNodes;
    }

    public EnergyProvider fluxNode(Set<FluxNode> fluxNodes) {
        this.fluxNodes = fluxNodes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnergyProvider energyProvider = (EnergyProvider) o;
        if (energyProvider.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), energyProvider.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EnergyProvider{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", purchaseRate=" + purchaseRate +
            ", purchaseAutoConsoRate=" + purchaseAutoConsoRate +
            ", fluxTopology=" + fluxTopologies +
            ", ps=" + ps +
            ", delta=" + delta +
            '}';
    }
}

