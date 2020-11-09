package fr.ifpen.synergreen.service.dto;

import fr.ifpen.synergreen.domain.enumeration.ParameterType;

public class ParameterDTO {

    private Long id;
    private String parameterLabel;
    private ParameterType parameterType;
    private String parameterValue;
    private FluxNodeDTO fluxNode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParameterLabel() {
        return parameterLabel;
    }

    public void setParameterLabel(String parameterLabel) {
        this.parameterLabel = parameterLabel;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public FluxNodeDTO getEnergyElement() {
        return fluxNode;
    }

    public void setEnergyElement(FluxNodeDTO fluxNode) {
        this.fluxNode = fluxNode;
    }
}
