package fr.ifpen.synergreen.service.dto;

import java.util.ArrayList;
import java.util.List;

public class SourceDescriptorDTO {

    private Long id;
    private List<ParameterDTO> descriptors = new ArrayList<>();
    private String source;
    private String label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ParameterDTO> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<ParameterDTO> descriptors) {
        this.descriptors = descriptors;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
