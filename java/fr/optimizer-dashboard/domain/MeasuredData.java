package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.DataType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class MeasuredData implements Serializable {

    private Long id;
    private ZonedDateTime instant;
    private DataType type;
    private Double measure;
    private Boolean valid;

    public MeasuredData() {
    }

    public MeasuredData(ZonedDateTime instant, Double measure, boolean valid, DataType type) {
        this.instant = instant;
        this.type = type;
        this.measure = measure;
        this.valid = valid;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public MeasuredData type(DataType type) {
        this.type = type;
        return this;
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    public MeasuredData measure(Double measure) {
        this.measure = measure;
        return this;
    }

    public Boolean isValid() {
        return valid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public MeasuredData valid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasuredData measuredData = (MeasuredData) o;
        if (measuredData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), measuredData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MeasuredData{" +
            "id=" + getId() +
            ", instant=" + getInstant() +
            ", type='" + getType() + "'" +
            ", measure=" + getMeasure() +
            ", valid='" + isValid() + "'" +
            "}";
    }

    public ZonedDateTime getInstant() {
        return instant;
    }

    public void setInstant(ZonedDateTime instant) {
        this.instant = instant;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
