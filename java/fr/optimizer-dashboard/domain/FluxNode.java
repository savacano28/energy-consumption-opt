package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A FluxNode
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //single table
@DiscriminatorColumn(name = "flux_node_type",
    discriminatorType = DiscriminatorType.STRING)
@Document(indexName = "fluxnode")
public abstract class FluxNode {

    @Transient
    public StateFluxNodeDTO stateFluxNodeDTO = new StateFluxNodeDTO();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    protected Long id;
    @ManyToOne
    @JoinColumn(name = "flux_topology_id")
    protected FluxTopology fluxTopology;
    @Column(name = "flux_node_name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private FluxNodeType type;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FluxGroup parent;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "measurement_source_id", referencedColumnName = "id")
    private MeasurementSource measurementSource;

    @OneToMany(mappedBy = "fluxNode")
    private List<Parameter> parameters = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "energy_provider_id")
    private EnergyProvider energyProvider = new EnergyProvider();

    /*getters and setters */
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

    public FluxGroup getParent() {
        return parent;
    }

    public void setParent(FluxGroup parent) {
        this.parent = parent;
    }

    public MeasurementSource getMeasurementSource() {
        return measurementSource;
    }

    public void setMeasurementSource(MeasurementSource measurementSource) {
        this.measurementSource = measurementSource;
    }

    public FluxTopology getFluxTopology() {
        return fluxTopology;
    }

    public void setFluxTopology(FluxTopology fluxTopology) {
        this.fluxTopology = fluxTopology;
    }

    public EnergyProvider getEnergyProvider() {
        return energyProvider;
    }

    public void setEnergyProvider(EnergyProvider energyProvider) {
        this.energyProvider = energyProvider;
    }

    public StateFluxNodeDTO getStateFluxNodeDTO() {
        return stateFluxNodeDTO;
    }

    public void setStateFluxNodeDTO(StateFluxNodeDTO stateFluxNodeDTO) {
        this.stateFluxNodeDTO = stateFluxNodeDTO;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /* methodes*/
    public EnergyProvider findProvider() {
        if (getEnergyProvider() != null) {
            return getEnergyProvider();
        } else {
            return getParent().findProvider();
        }
    }

    public abstract List<StateFluxNodeDTO> getAllStateChildren();

    public abstract List<FluxNode> getAllChildren();

    public abstract StateFluxNodeDTO getStateSummaryNode(FluxPeriod fP);

    public abstract void balancingState(Integer index,
                                        Double fConsoFromGrid,
                                        Double fConsoFromProd,
                                        Double fConsoFromBat,
                                        Double fProdConsByBat,
                                        Double fProdConsByConsumers,
                                        Double fProdSentToGrid,
                                        Double fBatConsFromGrid,
                                        Double fBatProdConsByCons
    );

}
