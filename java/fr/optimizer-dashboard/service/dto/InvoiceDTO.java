package fr.ifpen.synergreen.service.dto;

import fr.ifpen.synergreen.domain.FluxTopology;

import java.time.Month;

public class InvoiceDTO {

    private Long id;
    private FluxTopology fluxTopology;
    private Month month;
    private Integer year;
    private Double amount;
    private Double accAmount;
    private Long lastUpdateInvoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FluxTopology getFluxTopology() {
        return fluxTopology;
    }

    public void setFluxTopology(FluxTopology fluxTopology) {
        this.fluxTopology = fluxTopology;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAccAmount() {
        return accAmount;
    }

    public void setAccAmount(Double accAmount) {
        this.accAmount = accAmount;
    }

    public Long getLastUpdateInvoice() {
        return lastUpdateInvoice;
    }

    public void setLastUpdateInvoice(Long lastUpdateInvoice) {
        this.lastUpdateInvoice = lastUpdateInvoice;
    }
}
