package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.ParameterType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "parameter")
public class Parameter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "parameter_name")
    private String parameterName;

    @Column(name = "parameter_label")
    private String parameterLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "parameter_type")
    private ParameterType parameterType;

    @Column(name = "parameter_value")
    private String parameterValue;

    @Column(name = "parameter_unit")
    private String parameterUnit;

    @ManyToOne
    @JoinColumn(name = "flux_node_id")
    private FluxNode fluxNode;

    @ManyToOne
    @JoinColumn(name = "measurement_source_id")
    private MeasurementSource measurementSource;

    @ManyToOne
    @JoinColumn(name = "source_descriptor_id")
    private SourceDescriptor sourceDescriptor;

    public Parameter() {
    }

    public Parameter(String parameterName, String parameterLabel, ParameterType parameterType, String parameterValue, String parameterUnit) {
        this.parameterName = parameterName;
        this.parameterLabel = parameterLabel;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
        this.parameterUnit = parameterUnit;
    }

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

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public FluxNode getFluxNode() {
        return fluxNode;
    }

    public void setFluxNode(FluxNode fluxNode) {
        this.fluxNode = fluxNode;
    }

    public MeasurementSource getMeasurementSource() {
        return measurementSource;
    }

    public void setMeasurementSource(MeasurementSource measurementSource) {
        this.measurementSource = measurementSource;
    }

    public SourceDescriptor getSourceDescriptor() {
        return sourceDescriptor;
    }

    public void setSourceDescriptor(SourceDescriptor sourceDescriptor) {
        this.sourceDescriptor = sourceDescriptor;
    }
}
