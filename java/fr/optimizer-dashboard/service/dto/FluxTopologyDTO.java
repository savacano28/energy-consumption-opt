package fr.ifpen.synergreen.service.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluxTopologyDTO {

    private Long id;
    private String name;
    private boolean isOptimization;
    private FluxTopologyDTO parentTopology;
    private EnergySiteDTO energySite;
    private Set<FluxNodeDTO> children = new HashSet<>();
    private Set<FluxTopologyDTO> optimizations = new HashSet<>();
    private EnergyProviderDTO provider;
    private StateFluxNodeDTO stateFluxNodeDTO = new StateFluxNodeDTO();
    private List<FluxNodeDTO> allElementsChildren = new ArrayList<>();
    private List<InvoiceDTO> invoices = new ArrayList<>();
    private String img;

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

    public EnergySiteDTO getEnergySite() {
        return energySite;
    }

    public void setEnergySite(EnergySiteDTO energySite) {
        this.energySite = energySite;
    }

    public Set<FluxNodeDTO> getFluxNodes() {
        return children;
    }

    public void setFluxNodes(Set<FluxNodeDTO> children) {
        this.children = children;
    }

    public EnergyProviderDTO getProvider() {
        return provider;
    }

    public void setProvider(EnergyProviderDTO provider) {
        this.provider = provider;
    }

    public boolean isOptimization() {
        return isOptimization;
    }

    public void setOptimization(boolean optimization) {
        isOptimization = optimization;
    }

    public Set<FluxTopologyDTO> getOptimizations() {
        return optimizations;
    }

    public void setOptimizations(Set<FluxTopologyDTO> optimizations) {
        this.optimizations = optimizations;
    }

    public FluxTopologyDTO getParentTopology() {
        return parentTopology;
    }

    public void setParentTopology(FluxTopologyDTO parentTopology) {
        this.parentTopology = parentTopology;
    }

    public StateFluxNodeDTO getStateFluxNodeDTO() {
        return stateFluxNodeDTO;
    }

    public void setStateFluxNodeDTO(StateFluxNodeDTO stateFluxNodeDTO) {
        this.stateFluxNodeDTO = stateFluxNodeDTO;
    }

    public List<FluxNodeDTO> getAllElementsChildren() {
        return allElementsChildren;
    }

    public void setAllElementsChildren(List<FluxNodeDTO> allElementsChildren) {
        this.allElementsChildren = allElementsChildren;
    }

    public Set<FluxNodeDTO> getChildren() {
        return children;
    }

    public void setChildren(Set<FluxNodeDTO> children) {
        this.children = children;
    }

    /*Invoice*/

    public List<InvoiceDTO> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDTO> invoices) {
        this.invoices = invoices;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "FluxTopologyDTO{" +
            "id=" + id +
            ", topologyName='" + name + '\'' +
            ", energySite=" + energySite +
            '}';
    }
}
