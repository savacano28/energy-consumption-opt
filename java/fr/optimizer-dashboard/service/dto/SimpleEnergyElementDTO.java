package fr.ifpen.synergreen.service.dto;


import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;

public class SimpleEnergyElementDTO {

    private Long id;
    private String elementName;
    private FluxNodeType type;

    public SimpleEnergyElementDTO() {
    }

    public SimpleEnergyElementDTO(Long id, String elementName, FluxNodeType type) {
        this.id = id;
        this.elementName = elementName;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public FluxNodeType getType() {
        return type;
    }

    public void setType(FluxNodeType type) {
        this.type = type;
    }
}
