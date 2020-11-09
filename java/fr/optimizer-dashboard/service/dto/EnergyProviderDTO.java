package fr.ifpen.synergreen.service.dto;

import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class EnergyProviderDTO {

    private Long id;
    private String name;
    private Double purchaseRate;
    private Double purchaseAutoConsoRate;
    private Double sellLowHoursLowSeason;
    private Double sellLowHoursHighSeason;
    private Double sellPeakHoursLowSeason;
    private Double sellPeakHoursHighSeason;
    private Double sellHighHoursLowSeason;
    private Double sellHighHoursHighSeason;
    private Set<Month> monthsOffPeaks = new HashSet<>();
    private Set<Month> monthsHighSeasons = new HashSet<>();
    private Set<Integer> highHours = new HashSet<>();
    private Set<Integer> lowHours = new HashSet<>();
    private Set<Integer> peakHours = new HashSet<>();

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

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Double getPurchaseAutoConsoRate() {
        return purchaseAutoConsoRate;
    }

    public void setPurchaseAutoConsoRate(Double purchaseAutoConsoRate) {
        this.purchaseAutoConsoRate = purchaseAutoConsoRate;
    }

    public Double getSellLowHoursLowSeason() {
        return sellLowHoursLowSeason;
    }

    public void setSellLowHoursLowSeason(Double sellLowHoursLowSeason) {
        this.sellLowHoursLowSeason = sellLowHoursLowSeason;
    }

    public Double getSellLowHoursHighSeason() {
        return sellLowHoursHighSeason;
    }

    public void setSellLowHoursHighSeason(Double sellLowHoursHighSeason) {
        this.sellLowHoursHighSeason = sellLowHoursHighSeason;
    }

    public Double getSellPeakHoursLowSeason() {
        return sellPeakHoursLowSeason;
    }

    public void setSellPeakHoursLowSeason(Double sellPeakHoursLowSeason) {
        this.sellPeakHoursLowSeason = sellPeakHoursLowSeason;
    }

    public Double getSellPeakHoursHighSeason() {
        return sellPeakHoursHighSeason;
    }

    public void setSellPeakHoursHighSeason(Double sellPeakHoursHighSeason) {
        this.sellPeakHoursHighSeason = sellPeakHoursHighSeason;
    }

    public Double getSellHighHoursLowSeason() {
        return sellHighHoursLowSeason;
    }

    public void setSellHighHoursLowSeason(Double sellHighHoursLowSeason) {
        this.sellHighHoursLowSeason = sellHighHoursLowSeason;
    }

    public Double getSellHighHoursHighSeason() {
        return sellHighHoursHighSeason;
    }

    public void setSellHighHoursHighSeason(Double sellHighHoursHighSeason) {
        this.sellHighHoursHighSeason = sellHighHoursHighSeason;
    }

    public Set<Month> getMonthsOffPeaks() {
        return monthsOffPeaks;
    }

    public void setMonthsOffPeaks(Set<Month> monthsOffPeaks) {
        this.monthsOffPeaks = monthsOffPeaks;
    }

    public Set<Month> getMonthsHighSeasons() {
        return monthsHighSeasons;
    }

    public void setMonthsHighSeasons(Set<Month> monthsHighSeasons) {
        this.monthsHighSeasons = monthsHighSeasons;
    }

    public Set<Integer> getHighHours() {
        return highHours;
    }

    public void setHighHours(Set<Integer> highHours) {
        this.highHours = highHours;
    }

    public Set<Integer> getLowHours() {
        return lowHours;
    }

    public void setLowHours(Set<Integer> lowHours) {
        this.lowHours = lowHours;
    }

    public Set<Integer> getPeakHours() {
        return peakHours;
    }

    public void setPeakHours(Set<Integer> peakHours) {
        this.peakHours = peakHours;
    }

    @Override
    public String toString() {
        return "EnergyProviderDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", purchaseRate=" + purchaseRate +
            ", purchaseAutoConsoRate=" + purchaseAutoConsoRate +
            ", sellLowHoursLowSeason=" + sellLowHoursLowSeason +
            ", sellLowHoursHighSeason=" + sellLowHoursHighSeason +
            ", sellPeakHoursLowSeason=" + sellPeakHoursLowSeason +
            ", sellPeakHoursHighSeason=" + sellPeakHoursHighSeason +
            ", sellHighHoursLowSeason=" + sellHighHoursLowSeason +
            ", sellHighHoursHighSeason=" + sellHighHoursHighSeason +
            ", monthsOffPeaks=" + monthsOffPeaks +
            ", monthsHighSeasons=" + monthsHighSeasons +
            ", highHours=" + highHours +
            ", lowHours=" + lowHours +
            ", peakHours=" + peakHours +
            '}';
    }
}
