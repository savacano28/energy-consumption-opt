package fr.ifpen.synergreen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A BasicSource.
 */
@Entity
@DiscriminatorValue("OFFSET")
public class OffsetMeasurementSource extends MeasurementSource implements Serializable {

    @Transient
    MeasurementSource coreSource = new CSVSource();
    @Transient
    Integer offsetInSecond;

    @Transient
    private boolean parametersLoaded = false;

    public OffsetMeasurementSource(){
    }

    private Integer getOffset(){
        if(!parametersLoaded){
            coreSource.setParameters(getParameters());
            parametersLoaded = true;
        }
        if(offsetInSecond == null){
            String value = getParameters().stream().filter(p -> p.getParameterName().equals("offsetInSecond")).findFirst().get().getParameterValue();
            offsetInSecond = Integer.parseInt(value);
        }
        return offsetInSecond;
    }

    @Override
    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {
        if(!parametersLoaded){
            coreSource.setParameters(getParameters());
            parametersLoaded = true;
        }
        fP.setStart(fP.getStart().minusSeconds(getOffset()));
        fP.setEnd(fP.getEnd().minusSeconds(getOffset()));
        List<MeasuredData> measurements = coreSource.getPowersByPeriod(fP);
        if(measurements != null){
            return measurements
                .stream()
                .map(measuredData -> {
                    measuredData.setInstant(measuredData.getInstant().minusSeconds(getOffset()));
                    return measuredData;
                }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public MeasuredData getPowerInstant(ZonedDateTime t) {
        if(!parametersLoaded){
            coreSource.setParameters(getParameters());
            parametersLoaded = true;
        }
        return coreSource.getPowerInstant(t.minusSeconds(getOffset()));
    }

    @Override
    public Map<ZonedDateTime, Double> getPowerMeasurements() {
        return super.getPowerMeasurements();
    }

    @Override
    public String toString() {
        return "BasicSource{" +
            "id=" + getId() +
            "}";
    }

}
