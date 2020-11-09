package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Battery Manager manages the current list of battery control runs
 */
@Entity
@Table(name = "battery_manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "batterymanager")
public class BatteryManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "batteryManager", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<BatteryManagementRun> jobs = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id")
    @MapsId
    private FluxTopology fluxTopology;

    public BatteryManager() {
    }

    public BatteryManager(List<BatteryManagementRun> jobs) {
        this.jobs = jobs;
    }

    public BatteryManager(Long id, List<BatteryManagementRun> jobs, FluxTopology fluxTopology) {
        this.id = id;
        this.jobs = jobs;
        this.fluxTopology = fluxTopology;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BatteryManagementRun> getJobs() {
        return jobs;
    }

    public void setJobs(List<BatteryManagementRun> jobs) {
        this.jobs = jobs;
    }

    public FluxTopology getFluxTopology() {
        return fluxTopology;
    }

    public void setFluxTopology(FluxTopology fluxTopology) {
        this.fluxTopology = fluxTopology;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryManager that = (BatteryManager) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(jobs, that.jobs) &&
            Objects.equals(fluxTopology, that.fluxTopology);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobs, fluxTopology);
    }

    @Override
    public String toString() {
        return "BatteryManager{" +
            "id=" + id +
            ", jobs=" + jobs +
            ", fluxTopology=" + fluxTopology +
            '}';
    }
}
