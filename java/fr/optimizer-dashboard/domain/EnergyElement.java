package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.domain.enumeration.DataType;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A EnergyElement.
 */
@Entity
@DiscriminatorValue("EnergyElement")
public class EnergyElement extends FluxNode implements Serializable {

    /*methods*/

    /**
     * @return Power in a period
     * *
     */
    public StateFluxNodeDTO getStateSummaryNode(FluxPeriod fP) {
        final List<MeasuredData> measures = getPowersByPeriod(fP);
        stateFluxNodeDTO.setType(getType());
        stateFluxNodeDTO.setName(getName());
        stateFluxNodeDTO.setId(getId());
        stateFluxNodeDTO.setPowersByPeriod(measures);

        //con prod switch
        switch (getType()) {
            case CONSUMER:
                stateFluxNodeDTO.setpConsoGlobal(measures);
                break;
            case PRODUCER:
                /*filter <1.5*/
                measures.stream().map(c -> {
                    if (c.getMeasure() < 1.5) {
                        c.setMeasure(0.);
                    }
                    return c;
                }).collect(Collectors.toList());
                /*save data prod*/
                stateFluxNodeDTO.setpProdGlobal(measures);
                break;
            case BATTERY:
                stateFluxNodeDTO.setpBatGlobal(measures);
        }

        init(); //Initialize the rest of arrays
        return stateFluxNodeDTO;
    }

    private void init() {
        /*init array prodToSell according to element level */
        if (fluxTopology == null) {
            stateFluxNodeDTO.getPowersByPeriod().stream().forEach(c -> {
                stateFluxNodeDTO.getpConsoFromGrid().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpProdSentToGrid().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));

                stateFluxNodeDTO.getdStrg().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpConsoFromBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpConsoFromProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpProdConsByBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpBConsoFromGrid().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                stateFluxNodeDTO.getpBProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));

                if (getType().equals(FluxNodeType.CONSUMER)) {
                    stateFluxNodeDTO.getpProdGlobal().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getdProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getdCons().add(new MeasuredData(c.getInstant(), c.getMeasure(), true, DataType.POWER));
                } else if (getType().equals(FluxNodeType.PRODUCER)) {
                    stateFluxNodeDTO.getpConsoGlobal().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getdProd().add(new MeasuredData(c.getInstant(), c.getMeasure(), true, DataType.POWER));
                    stateFluxNodeDTO.getdCons().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                }
            });

        } else {
            if (getType().equals(FluxNodeType.CONSUMER)) {
                stateFluxNodeDTO.getPowersByPeriod().stream().forEach(c -> {
                    stateFluxNodeDTO.getpProdGlobal().add(new MeasuredData(c.getInstant(), 0., true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromGrid().add(new MeasuredData(c.getInstant(), c.getMeasure(), true, DataType.POWER));
                    stateFluxNodeDTO.getpProdSentToGrid().add(new MeasuredData(c.getInstant(), 0., true, DataType.POWER));
                    stateFluxNodeDTO.getdProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getdCons().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));

                    stateFluxNodeDTO.getdStrg().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpProdConsByBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpBConsoFromGrid().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpBProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                });
            } else if (getType().equals(FluxNodeType.PRODUCER)) {
                stateFluxNodeDTO.getPowersByPeriod().stream().forEach(c -> {
                    stateFluxNodeDTO.getpConsoGlobal().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromGrid().add(new MeasuredData(c.getInstant(), 0., true, DataType.POWER));
                    stateFluxNodeDTO.getpProdSentToGrid().add(new MeasuredData(c.getInstant(), c.getMeasure(), true, DataType.POWER));
                    stateFluxNodeDTO.getdProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getdCons().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));

                    stateFluxNodeDTO.getdStrg().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpConsoFromProd().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpProdConsByBat().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpBConsoFromGrid().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                    stateFluxNodeDTO.getpBProdConsByConsumers().add(new MeasuredData(c.getInstant(), 0.0, true, DataType.POWER));
                });
            }
        }
    }

    public void balancingState(Integer index, Double fConsoFromGrid, Double fConsoFromProd, Double fConsoFromBat, Double fProdConsByBat, Double fProdConsByConsumers, Double fProdSentToGrid, Double fBatConsFromGrid, Double fBatProdConsByCons) {
        //add facteurs de correction de consommation et production
        if (getType().equals(FluxNodeType.CONSUMER)) {
            Double cons = stateFluxNodeDTO.getpConsoGlobal().get(index).getMeasure();
            Double consoFromGrid, consoFromProd, dCons;

            dCons = stateFluxNodeDTO.getdCons().get(index).getMeasure();
            consoFromGrid = stateFluxNodeDTO.getpConsoFromGrid().get(index).getMeasure();
            consoFromProd = stateFluxNodeDTO.getpConsoFromProd().get(index).getMeasure();

            stateFluxNodeDTO.getpConsoFromGrid().get(index).setMeasure(consoFromGrid + fConsoFromGrid * dCons);
            stateFluxNodeDTO.getpConsoFromProd().get(index).setMeasure(consoFromProd + fConsoFromProd * dCons);
            stateFluxNodeDTO.getpConsoFromBat().get(index).setMeasure(fConsoFromBat * dCons);

            dCons = cons - (
                stateFluxNodeDTO.getpConsoFromGrid().get(index).getMeasure() +
                    stateFluxNodeDTO.getpConsoFromProd().get(index).getMeasure() +
                    stateFluxNodeDTO.getpConsoFromBat().get(index).getMeasure());

            stateFluxNodeDTO.getdCons().get(index).setMeasure(dCons);

        } else if (getType().equals(FluxNodeType.PRODUCER)) {
            Double prod = stateFluxNodeDTO.getpProdGlobal().get(index).getMeasure();
            Double prodConsByConsumers, prodSentToGrid, dProd;

            dProd = stateFluxNodeDTO.getdProd().get(index).getMeasure();
            prodConsByConsumers = stateFluxNodeDTO.getpProdConsByConsumers().get(index).getMeasure();
            prodSentToGrid = stateFluxNodeDTO.getpProdSentToGrid().get(index).getMeasure();

            stateFluxNodeDTO.getpProdSentToGrid().get(index).setMeasure(prodSentToGrid + fProdSentToGrid * dProd);
            stateFluxNodeDTO.getpProdConsByConsumers().get(index).setMeasure(prodConsByConsumers + fProdConsByConsumers * dProd);
            stateFluxNodeDTO.getpProdConsByBat().get(index).setMeasure(fProdConsByBat * dProd);

            dProd = prod - (
                stateFluxNodeDTO.getpProdSentToGrid().get(index).getMeasure() +
                    stateFluxNodeDTO.getpProdConsByConsumers().get(index).getMeasure() +
                    stateFluxNodeDTO.getpProdConsByBat().get(index).getMeasure());

            stateFluxNodeDTO.getdProd().get(index).setMeasure(dProd);

        } else { //todo
            Double bat = stateFluxNodeDTO.getpBatGlobal().get(index).getMeasure();
          /*  stateFluxNodeDTO.getpBConsoFromGrid().add(new MeasuredData(current, fBatConsFromGrid * bat, true, DataType.POWER));
            stateFluxNodeDTO.getpBProdConsByConsumers().add(new MeasuredData(current, fBatProdConsByCons * bat, true, DataType.POWER));*/
        }
    }

    public List<StateFluxNodeDTO> getAllStateChildren() {
        return Collections.singletonList(stateFluxNodeDTO);
    }

    public List<FluxNode> getAllChildren() {
        return Collections.singletonList(this);
    }

   /* public void setInstruction(Instruction instruction) {
    }*/

    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {
        if (getMeasurementSource() == null) {
            throw new SynergreenException("You must set a power sensor to " + getName());
        }
        return getMeasurementSource().getPowersByPeriod(fP);
    }

}
