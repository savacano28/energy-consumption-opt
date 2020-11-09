package fr.ifpen.synergreen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BasicSource.
 */
@Entity
@DiscriminatorValue("BASIC")
public class BasicSource extends MeasurementSource implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicSource basicSource = (BasicSource) o;
        if (basicSource.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), basicSource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BasicSource{" +
            "id=" + getId() +
            "}";
    }

   /* public void setInstruction(Instruction instruction) {

    }*/

}
