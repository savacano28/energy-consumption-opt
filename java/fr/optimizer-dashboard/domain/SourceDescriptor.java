package fr.ifpen.synergreen.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A MeasurementSource.
 */
@Entity
@Table(name = "source_descriptor")
@Document(indexName = "sourcedescriptor")
public class SourceDescriptor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    protected Long id;

    @OneToMany(mappedBy = "sourceDescriptor")
    private List<Parameter> descriptors = new ArrayList<>();

    @Column(name = "source")
    private String source;

    @Column(name = "label")
    private String label;

    public SourceDescriptor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Parameter> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<Parameter> descriptors) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SourceDescriptor that = (SourceDescriptor) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
