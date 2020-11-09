package fr.ifpen.synergreen.service.dto;

import java.util.ArrayList;
import java.util.List;

public class MeasurementSourceDTO {

    private Long id;
    private List<ParameterDTO> parameters = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDTO> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "MeasurementSourceDTO{" +
            "id=" + id +
            '}';
    }
}
