package fr.ifpen.synergreen.service.dto;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatteryManagementSourceDTO {

    private final Logger log = LoggerFactory.getLogger(BatteryManagementSourceDTO.class);

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
    private List<BatteryManagementPredictionDTO> dataset;

    public Double getEm() {
        return Em;
    }

    public void setEm(Double em) {
        Em = em;
    }

    public Double getEp() {
        return Ep;
    }

    public void setEp(Double ep) {
        Ep = ep;
    }

    public Double getEpm() {
        return Epm;
    }

    public void setEpm(Double epm) {
        Epm = epm;
    }

    public Double getEpp() {
        return Epp;
    }

    public void setEpp(Double epp) {
        Epp = epp;
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

    public void setPcp(List<Double> pcp) {
        Pcp = pcp;
    }

    public List<Double> getPdp() {
        return Pdp;
    }

    public void setPdp(List<Double> pdp) {
        Pdp = pdp;
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

    public void setE0(Double e0) {
        E0 = e0;
    }

    public Double getE0tfFactor() {
        return E0tfFactor;
    }

    public void setE0tfFactor(Double e0tfFactor) {
        E0tfFactor = e0tfFactor;
    }

    public Double getEtf() {
        return Etf;
    }

    public void setEtf(Double etf) {
        Etf = etf;
    }

    public Double getPs() {
        return Ps;
    }

    public void setPs(Double ps) {
        Ps = ps;
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

    public void setT(Double t) {
        T = t;
    }

    public Double getDT() {
        return DT;
    }

    public void setDT(Double DT) {
        this.DT = DT;
    }

    public List<BatteryManagementPredictionDTO> getDataset() {
        return dataset;
    }

    public void setDataset(List<BatteryManagementPredictionDTO> dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "BatteryManagementSourceDTO{" +
            "Em=" + Em +
            ", Ep=" + Ep +
            ", Epm=" + Epm +
            ", Epp=" + Epp +
            ", batteryType='" + batteryType + '\'' +
            ", vecEPlim=" + vecEPlim +
            ", Pcp=" + Pcp +
            ", Pdp=" + Pdp +
            ", rhoC=" + rhoC +
            ", rhoD=" + rhoD +
            ", E0=" + E0 +
            ", E0tfFactor=" + E0tfFactor +
            ", Etf=" + Etf +
            ", Ps=" + Ps +
            ", alphaPeak=" + alphaPeak +
            ", T=" + T +
            ", DT=" + DT +
            '}';
    }


    public void toJson(String jsonInputFilePath) {

        ObjectMapper Obj = new ObjectMapper();
        ObjectWriter writer = Obj.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(new File(jsonInputFilePath), this);
        } catch (IOException e) {
            log.error("IOException on writing BatteryManagementSourceDTO to input JSON file " + e);
        }
    }
}
