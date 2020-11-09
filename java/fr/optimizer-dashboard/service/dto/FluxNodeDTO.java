package fr.ifpen.synergreen.service.dto;


import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluxNodeDTO {

    private Long id;
    private String name;
    private FluxNodeType type;
    private MeasurementSourceDTO measurementSource; /*TODO reviser*/
    private StateFluxNodeDTO stateFluxNodeDTO;
    private List<StateFluxNodeDTO> children;
    private Set<EnergyProviderDTO> providers = new HashSet<>();

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

    public FluxNodeType getType() {
        return type;
    }

    public void setType(FluxNodeType type) {
        this.type = type;
    }

    public MeasurementSourceDTO getMeasurementSource() {
        return measurementSource;
    }

    public void setMeasurementSource(MeasurementSourceDTO measurementSource) {
        this.measurementSource = measurementSource;
    }

    public StateFluxNodeDTO getStateFluxNodeDTO() {
        return stateFluxNodeDTO;
    }

    public void setStateFluxNodeDTO(StateFluxNodeDTO stateFluxNodeDTO) {
        this.stateFluxNodeDTO = stateFluxNodeDTO;
    }

    public List<StateFluxNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<StateFluxNodeDTO> children) {
        this.children = children;
    }

    public Set<EnergyProviderDTO> getProviders() {
        return providers;
    }

    public void setProviders(Set<EnergyProviderDTO> providers) {
        this.providers = providers;
    }

    @Override
    public String toString() {
        return "FluxNodeDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", measurementSource=" + measurementSource +
            '}';
    }
}
