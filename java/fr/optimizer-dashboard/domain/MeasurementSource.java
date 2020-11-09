package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.DataType;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * A MeasurementSource.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //single table
@DiscriminatorColumn(name = "source_type",
    discriminatorType = DiscriminatorType.STRING)
@Document(indexName = "measurementsource")
public abstract class MeasurementSource {

    @OneToOne(mappedBy = "measurementSource")
    public FluxNode fluxNode;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    protected Long id;
    @Transient
    protected Map<ZonedDateTime, Double> powerMeasurements = new LinkedHashMap<>();
    @OneToMany(mappedBy = "measurementSource")
    private List<Parameter> parameters = new ArrayList<>();
    @Transient
    private List<ZonedDateTime> localDateTimes;

    public MeasurementSource() {
    }

    //public abstract void setInstruction(Instruction instruction);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FluxNode getFluxNode() {
        return fluxNode;
    }

    public void setFluxNode(FluxNode fluxNode) {
        this.fluxNode = fluxNode;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /*Power Methods*/ //FIXME
    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {
        List<MeasuredData> measuredDataList = new ArrayList<>();
        ZonedDateTime current = fP.getStart();
        Long currentStep = fP.getStep();
        localDateTimes = new ArrayList<>(powerMeasurements.keySet());

        while (currentStep > 0/* current.isBefore(fP.getEnd())*/) {
            MeasuredData m = getPowerInstant(current);
            if (m.getValid()) {
                measuredDataList.add(m);
            }
            currentStep = Math.min(fP.getStep(), SECONDS.between(current, fP.getEnd()));
            current = current.plus(currentStep, SECONDS);
        }
        return measuredDataList;
    }

    public MeasuredData getPowerInstant(ZonedDateTime t) {
        if (getPowerMeasurements().containsKey(t)) {
            return new MeasuredData(t, getPowerMeasurements().get(t), true, DataType.POWER); //
        } else {
            return estimateValue(t, DataType.POWER);
        }
    }

    public Map<ZonedDateTime, Double> getPowerMeasurements() {
        return powerMeasurements;
    }

    public void setPowerMeasurements(Map<ZonedDateTime, Double> powerMeasurements) {
        this.powerMeasurements = powerMeasurements;
    }

    private MeasuredData estimateValue(ZonedDateTime t, DataType d) {
        if (localDateTimes == null || localDateTimes.isEmpty()) {
            return new MeasuredData(t, 0.0, false, d);
        }
        if (t.isAfter(localDateTimes.get(0)) && t.isBefore(localDateTimes.get(localDateTimes.size() - 1))) {
            int closestValue = Math.abs(Collections.binarySearch(localDateTimes, t) + 1);//return position where item should be, +1 return prevoius position
            return new MeasuredData(t, innerDate(localDateTimes.get(closestValue - 1), localDateTimes.get(closestValue)),
                true, d);
        } else {
            return new MeasuredData(t, outerDate(t), false, d);
        }
    }

    /*Methode qui permet calculer le value de puissance si on connais les values plus proches à lui */
    private Double innerDate(ZonedDateTime a, ZonedDateTime b) {
        //interpolate
        return (powerMeasurements.get(a) + powerMeasurements.get(b)) / 2;
    }

    /*Methode qui permet calculer le value de puissance si le value n'existe pas, pour l'instante on a mis NO Value*/
    private Double outerDate(ZonedDateTime t) {
        //modele
        return 0.;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MeasurementSource that = (MeasurementSource) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
