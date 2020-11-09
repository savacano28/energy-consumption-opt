package fr.ifpen.synergreen.service.dto;

import java.util.HashSet;
import java.util.Set;

public class EnergySiteDTO {

    private Long id;
    private String name;
    private EnergyManagementSystemDTO energyManagementSystemDTO;
    private Set<FluxTopologyDTO> fluxTopologies = new HashSet<>();

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

    public EnergyManagementSystemDTO getEnergyManagementSystem() {
        return energyManagementSystemDTO;
    }

    public void setEnergyManagementSystem(EnergyManagementSystemDTO energyManagementSystemDTO) {
        this.energyManagementSystemDTO = energyManagementSystemDTO;
    }

    public Set<FluxTopologyDTO> getFluxTopologies() {
        return fluxTopologies;
    }

    public void setFluxTopologies(Set<FluxTopologyDTO> fluxTopologies) {
        this.fluxTopologies = fluxTopologies;
    }

    @Override
    public String toString() {
        return "EnergyGroupDTO{" +
            "id=" + id +
            ", groupName='" + name + '\'' +
            ", fluxTopologies=" + fluxTopologies +
            '}';
    }
}
