package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * A Battery management entity containing all the entries necessary for the Battery Management Service to compute new instructions for the next period of observation.
 */
public class BatteryManagementSource implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(BatteryManagementSource.class);

    // Déclaration des variables d'entrée de l'algo de pilotage
    private Double Em;
    private Double Ep;
    private Double Epm;
    private Double Epp;
    private String batteryType;
    private List<Double> vecEPlim = new ArrayList<>();
    private List<Double> Pcp = new ArrayList<>();
    private List<Double> Pdp = new ArrayList<>();
    private Double rhoC;
    private Double rhoD;
    private Double E0;
    private Double E0tfFactor;
    private Double Etf;
    private Double Ps;
    private Double alphaPeak;
    private Double T;
    private Double DT;
    private List<BatteryManagementPrediction> dataset = new ArrayList<>();


    public BatteryManagementSource() {
    }

    public BatteryManagementSource(Double Em, Double Ep, Double Epm, Double Epp,
                                   String batteryType, List<Double> vecEPlim, List<Double> Pcp, List<Double> Pdp,
                                   Double rhoC, Double rhoD,
                                   Double E0, Double E0tfFactor, Double Etf,
                                   Double Ps, Double alphaPeak,
                                   Double T, Double DT,
                                   List<BatteryManagementPrediction> dataset) {
        this.Em = Em;
        this.Ep = Ep;
        this.Epm = Epm;
        this.Epp = Epp;
        this.batteryType = batteryType;
        this.vecEPlim = vecEPlim;
        this.Pcp = Pcp;
        this.Pdp = Pdp;
        this.rhoC = rhoC;
        this.rhoD = rhoD;
        this.E0 = E0;
        this.E0tfFactor = E0tfFactor;
        this.Etf = Etf;
        this.Ps = Ps;
        this.alphaPeak = alphaPeak;
        this.T = T;
        this.DT = DT;
        this.dataset = dataset;
    }

    public BatteryManagementSource(ZonedDateTime currentTime,
                                   EnergyProvider energyProvider,
                                   FluxTopology fluxTopology,
                                   FluxNode battery,
                                   Double SoC0) {

        log.debug("Filling in batteryManagementSource...");
        log.debug("--> Inserting battery {} properties", battery.getId());
        this.Em = Double.valueOf(battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("Em")).findFirst().get().getParameterValue());
        this.Ep = Double.valueOf(battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("Ep")).findFirst().get().getParameterValue());
        this.Epm = Double.valueOf(battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("Epm")).findFirst().get().getParameterValue());
        this.Epp = Double.valueOf(battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("Epp")).findFirst().get().getParameterValue());
        this.batteryType = battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("BatteryType")).findFirst().get().getParameterValue();


        // Get Battery properties : Additional parameters => All those depend on the battery type. Default is Li-CmA

        log.debug("--> Inserting battery type details");
        switch (this.batteryType) {
            case ("Li"):
                this.vecEPlim = Arrays.asList(Double.valueOf(0.), this.Ep);
                this.Pcp = Arrays.asList(Double.valueOf(10.), Double.valueOf(10.));
                this.Pdp = Arrays.asList(Double.valueOf(10.), Double.valueOf(10.));
                this.rhoC = Double.valueOf(0.8);
                this.rhoD = Double.valueOf(0.8);
                break;
            case ("redox1"):
                this.vecEPlim = Arrays.asList(Double.valueOf(0.), this.Ep);
                this.Pcp = Arrays.asList(Double.valueOf(10.), Double.valueOf(10.));
                this.Pdp = Arrays.asList(Double.valueOf(10.), Double.valueOf(10.));
                this.rhoC = Double.valueOf(0.8);
                this.rhoD = Double.valueOf(0.8);
                break;
            // Other Cases to come...
        }

        //Get energy provider details
        log.debug("--> Inserting energy provider details");
        this.Ps = Double.valueOf(energyProvider.getPs());
        this.alphaPeak = Double.valueOf(energyProvider.getDelta());

        // Get Run details
        log.debug("--> Inserting battery management algo properties");
        try (InputStream input = new FileInputStream("src/main/resources/.batteryManagementAlgo.properties")) {

            // load the properties file and get the property values
            Properties prop = new Properties();
            prop.load(input);
            this.T = Double.valueOf(prop.getProperty("T"));
            this.DT = Double.valueOf(prop.getProperty("DT"));
            this.E0tfFactor = Double.valueOf(prop.getProperty("E0tfFactor"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Get battery's current state
        log.debug("--> Inserting battery's current state");
        this.E0 = SoC0 * this.Ep;
        this.Etf = this.E0 * this.E0tfFactor;


        // Get dataset content
        log.debug("--> Inserting prod and conso forecasts along with pricing detail");
        // time window  : from J(currentTime + DT) up until J+1(currentTime)
        // However, since we don't have prod and conso forecasting services yet, we'll really take measurements
        // from J-2(currentTime + DT) up to J-1(currentTime) (we consider J-2 to J-1 instead of J-1 to J because
        // there's a risk that measured data from Historian may not be up to date as of now.
        // By taking J-2, we are more or less sure that power data will be available in the DB

        // Fluxtopology : get Balance data during the period of observation T
        // TODO change to real T start (currentTime + DT) and real end T (currentTime + T)
        //  as soon as forecasting models will be made available. Until then, see above for the approximations made...
        Long longDT = Math.round(this.getDT());
        Long longT = Math.round(this.getT());
        Long TwiceAsLongT = longT * 2;
        ZonedDateTime start = currentTime.minusMinutes(TwiceAsLongT - longDT);  // J-2(currentTime + DT)
        ZonedDateTime end = currentTime.minusMinutes(longT);    // J-1(currentTime)
        FluxPeriod fluxPeriod = new FluxPeriod(start, end, longDT * 60);
        List<StateFluxNodeDTO> listOfStates = fluxTopology.getStateSummary(fluxPeriod);

        // Complete dataset with data
        List<BatteryManagementPrediction> listOfPredictions = new ArrayList<>();
        if (listOfStates.size() != 0) {
            int measureCount = listOfStates.get(0).getpProdGlobal().size();
            BatteryManagementPrediction prediction;
            ZonedDateTime zonedDatetimePrediction = start;
            for (int i = 0; i < measureCount; i++) {
                prediction = new BatteryManagementPrediction();
                prediction.setInstant(zonedDatetimePrediction);
                prediction.setpProdGlobal(listOfStates.get(0).getpProdGlobal().get(i).getMeasure());
                prediction.setpConsoGlobal(listOfStates.get(0).getpConsoGlobal().get(i).getMeasure());
                prediction.setPrixAchat(fluxTopology.getPriceEnergy(energyProvider, listOfStates.get(0).getpProdGlobal().get(i).getInstant()));
                prediction.setPrixVente(energyProvider.getPurchaseRate());
                listOfPredictions.add(prediction);
                zonedDatetimePrediction = zonedDatetimePrediction.plusMinutes(longDT);
            }
            this.dataset = listOfPredictions;
        }
    }


//    private String getParameterValueByLabel(List<Parameter> parameters, String label) {
//        String parameterValue = "Not found";
//        for (Parameter parameter : parameters) {
//            if (label == parameter.getParameterLabel()) {
//                parameterValue = parameter.getParameterValue();
//                return parameterValue;
//            }
//        }
//        return parameterValue;
//    }


    public Double getEm() {
        return Em;
    }

    public void setEm(Double Em) {
        this.Em = Em;
    }

    public Double getEp() {
        return Ep;
    }

    public void setEp(Double Ep) {
        this.Ep = Ep;
    }

    public Double getEpm() {
        return Epm;
    }

    public void setEpm(Double Epm) {
        this.Epm = Epm;
    }

    public Double getEpp() {
        return Epp;
    }

    public void setEpp(Double Epp) {
        this.Epp = Epp;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    public List<Double> getVecEPlim() {
        return vecEPlim;
    }

    public void setVecEPlim(List<Double> vecEPlim) {
        this.vecEPlim = vecEPlim;
    }

    public List<Double> getPcp() {
        return Pcp;
    }

    public void setPcp(List<Double> Pcp) {
        this.Pcp = Pcp;
    }

    public List<Double> getPdp() {
        return Pdp;
    }

    public void setPdp(List<Double> Pdp) {
        this.Pdp = Pdp;
    }

    public Double getRhoC() {
        return rhoC;
    }

    public void setRhoC(Double rhoC) {
        this.rhoC = rhoC;
    }

    public Double getRhoD() {
        return rhoD;
    }

    public void setRhoD(Double rhoD) {
        this.rhoD = rhoD;
    }

    public Double getE0() {
        return E0;
    }

    public void setE0(Double E0) {
        this.E0 = E0;
    }

    public Double getEtf() {
        return Etf;
    }

    public void setEtf(Double Etf) {
        this.Etf = Etf;
    }

    public Double getE0tfFactor() {
        return E0tfFactor;
    }

    public void setE0tfFactor(Double e0tfFactor) {
        E0tfFactor = e0tfFactor;
    }

    public Double getPs() {
        return Ps;
    }

    public void setPs(Double Ps) {
        this.Ps = Ps;
    }

    public Double getAlphaPeak() {
        return alphaPeak;
    }

    public void setAlphaPeak(Double alphaPeak) {
        this.alphaPeak = alphaPeak;
    }

    public Double getT() {
        return T;
    }

    public void setT(Double T) {
        this.T = T;
    }

    public Double getDT() {
        return DT;
    }

    public void setDT(Double T) {
        this.DT = DT;
    }

    public List<BatteryManagementPrediction> getDataset() {
        return dataset;
    }

    public void setDataset(List<BatteryManagementPrediction> dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "BatteryManagementSource{" +
            "Em=" + getEm() +
            ", Ep=" + getEp() +
            ", Epm=" + getEpm() +
            ", Epp=" + getEpp() +
            ", batteryType='" + getBatteryType() + "'" +
            ", vecEPlim=" + getVecEPlim() +
            ", Pcp=" + getPcp() +
            ", Pdp=" + getPdp() +
            ", rhoC=" + getRhoC() +
            ", rhoD=" + getRhoD() +
            ", E0=" + getE0() +
            ", Etf=" + getEtf() +
            ", Ps=" + getPs() +
            ", alphaPeak=" + getAlphaPeak() +
            ", T=" + getT() +
            ", DT=" + getDT() +
            ", dataset=" + getDataset() +
            "}";
    }


    public void toJson(String jsonInputFilePath) {

        ObjectMapper Obj = new ObjectMapper();
        ObjectWriter writer = Obj.writer();

        try {
            writer.writeValue(new File(jsonInputFilePath), this);
        } catch (IOException e) {
            log.error("IOException on writing BatteryManagementSource to input JSON file " + e);
        }
    }
}
