package fr.ifpen.synergreen.service.dto;

import java.util.HashSet;
import java.util.Set;

public class EnergyManagementSystemDTO {

    private Long id;
    private String name;
    private Set<EnergySiteDTO> energySites = new HashSet<>();

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

    public Set<EnergySiteDTO> getEnergySites() {
        return energySites;
    }

    public void setEnergySites(Set<EnergySiteDTO> energySites) {
        this.energySites = energySites;
    }

    @Override
    public String toString() {
        return "EnergyManagementSystemDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", energySites=" + energySites +
            '}';
    }
}
