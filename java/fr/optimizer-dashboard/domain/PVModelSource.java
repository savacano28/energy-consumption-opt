package fr.ifpen.synergreen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A PVModelSource.
 */
@Entity
@DiscriminatorValue("PV")
public class PVModelSource extends MeasurementSource implements Serializable {


    public MeasuredData getPower(ZonedDateTime t) {
        return null;
    }

//    @Override
//    public void setInstruction(Instruction instruction) {
//    }

    @Override
    public String toString() {
        return "PVModelSource{" +
            "id=" + getId() +
            "}";
    }


}
