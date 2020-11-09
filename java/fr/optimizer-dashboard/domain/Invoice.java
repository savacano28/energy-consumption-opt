package fr.ifpen.synergreen.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Month;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name = "flux_topology_id")
    protected FluxTopology fluxTopology;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    @Column(name = "month")
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private Month month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "acc_amount")
    private Double accAmount;

    @Column(name = "last_update_invoice")
    private Long lastUpdateInvoice;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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
