package fr.ifpen.synergreen.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A EnergySite.
 */
@Entity
@Table(name = "energy_site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "energysite")
public class EnergySite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "energySite")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FluxTopology> fluxTopologies = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "energy_management_system_id")
    private EnergyManagementSystem energyManagementSystem;


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

    public EnergySite name(String name) {
        this.name = name;
        return this;
    }

    public Set<FluxTopology> getFluxTopologies() {
        return fluxTopologies;
    }

    public void setFluxTopologies(Set<FluxTopology> fluxTopologies) {

        this.fluxTopologies = fluxTopologies;
    }

    public EnergySite fluxTopologies(Set<FluxTopology> fluxTopologies) {
        this.fluxTopologies = fluxTopologies;
        return this;
    }

    public EnergySite addFluxTopology(FluxTopology fluxTopology) {
        this.fluxTopologies.add(fluxTopology);
        fluxTopology.setEnergySite(this);
        return this;
    }

    public EnergySite removeFluxTopology(FluxTopology fluxTopology) {
        this.fluxTopologies.remove(fluxTopology);
        fluxTopology.setEnergySite(null);
        return this;
    }

    public EnergyManagementSystem getEnergyManagementSystem() {
        return energyManagementSystem;
    }

    public void setEnergyManagementSystem(EnergyManagementSystem energyManagementSystem) {
        this.energyManagementSystem = energyManagementSystem;
    }

    public EnergySite energyManagementSystem(EnergyManagementSystem energyManagementSystem) {
        this.energyManagementSystem = energyManagementSystem;
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
        EnergySite energySite = (EnergySite) o;
        if (energySite.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), energySite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EnergySite{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
