package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A EnergyManagementSystem.
 */
@Entity
@Table(name = "energy_management_system")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "energymanagementsystem")
public class EnergyManagementSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "energyManagementSystem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EnergySite> energySites = new HashSet<>();

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

    public EnergyManagementSystem name(String name) {
        this.name = name;
        return this;
    }

    public Set<EnergySite> getEnergySites() {
        return energySites;
    }

    public void setEnergySites(Set<EnergySite> energySites) {
        this.energySites = energySites;
    }

    public EnergyManagementSystem energySites(Set<EnergySite> energySites) {
        this.energySites = energySites;
        return this;
    }

    public EnergyManagementSystem addEnergySite(EnergySite energySite) {
        this.energySites.add(energySite);
        energySite.setEnergyManagementSystem(this);
        return this;
    }

    public EnergyManagementSystem removeEnergySite(EnergySite energySite) {
        this.energySites.remove(energySite);
        energySite.setEnergyManagementSystem(null);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnergyManagementSystem energyManagementSystem = (EnergyManagementSystem) o;
        if (energyManagementSystem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), energyManagementSystem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EnergyManagementSystem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
