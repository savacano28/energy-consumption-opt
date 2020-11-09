package fr.ifpen.synergreen.service.dto;


import fr.ifpen.synergreen.domain.MeasuredData;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateFluxNodeDTO {
    private FluxNodeType type;
    private String name;
    private Long id;

    //to save an power element data
    private List<MeasuredData> powersByPeriod = new ArrayList<>();

    //Measures by group/sub group
    //consumer unit
    private List<MeasuredData> pConsoGlobal = new ArrayList<>();
    private List<MeasuredData> pConsoFromProd = new ArrayList<>();
    private List<MeasuredData> pConsoFromGrid = new ArrayList<>();
    private List<MeasuredData> pConsoFromBat = new ArrayList<>();
    //production unit
    private List<MeasuredData> pProdGlobal = new ArrayList<>();
    private List<MeasuredData> pProdSentToGrid = new ArrayList<>();
    private List<MeasuredData> pProdConsByConsumers = new ArrayList<>();
    private List<MeasuredData> pProdConsByBat = new ArrayList<>();
    //battery unit
    private List<MeasuredData> pBatGlobal = new ArrayList<>();
    private List<MeasuredData> pBatLoadFromOthers = new ArrayList<>();
    private List<MeasuredData> pBatFedToOthers = new ArrayList<>(); //fixme i  need it?
    //bat consommation sources
    private List<MeasuredData> pBConsoFromGrid = new ArrayList<>();
    private List<MeasuredData> pBConsoFromBat = new ArrayList<>(); //fixme I still can not calculate it, I need to identify every bat
    //bat distribution production
    private List<MeasuredData> pBProdConsByConsumers = new ArrayList<>();
    private List<MeasuredData> pBProdConsByBat = new ArrayList<>(); //fixme I still can not calculate it,
    //prix
    private Map<String, Double> priceCons = new HashMap<>(); //change Big
    private Map<String, Double> priceProdSell = new HashMap<>();
    //delta
    private List<MeasuredData> dProd = new ArrayList<>();
    private List<MeasuredData> dCons = new ArrayList<>();
    private List<MeasuredData> dStrg = new ArrayList<>();

    /*getters and setters*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FluxNodeType getType() {
        return type;
    }

    public void setType(FluxNodeType type) {
        this.type = type;
    }

    public List<MeasuredData> getPowersByPeriod() {
        return powersByPeriod;
    }

    public void setPowersByPeriod(List<MeasuredData> powersByPeriod) {
        this.powersByPeriod = powersByPeriod;
    }

    public List<MeasuredData> getpConsoGlobal() {
        return pConsoGlobal;
    }

    public void setpConsoGlobal(List<MeasuredData> pConsoGlobal) {
        this.pConsoGlobal = pConsoGlobal;
    }

    public List<MeasuredData> getpConsoFromProd() {
        return pConsoFromProd;
    }

    public void setpConsoFromProd(List<MeasuredData> pConsoFromProd) {
        this.pConsoFromProd = pConsoFromProd;
    }

    public List<MeasuredData> getpConsoFromGrid() {
        return pConsoFromGrid;
    }

    public void setpConsoFromGrid(List<MeasuredData> pConsoFromGrid) {
        this.pConsoFromGrid = pConsoFromGrid;
    }

    public List<MeasuredData> getpConsoFromBat() {
        return pConsoFromBat;
    }

    public void setpConsoFromBat(List<MeasuredData> pConsoFromBat) {
        this.pConsoFromBat = pConsoFromBat;
    }

    public List<MeasuredData> getpProdGlobal() {
        return pProdGlobal;
    }

    public void setpProdGlobal(List<MeasuredData> pProdGlobal) {
        this.pProdGlobal = pProdGlobal;
    }

    public List<MeasuredData> getpProdSentToGrid() {
        return pProdSentToGrid;
    }

    public void setpProdSentToGrid(List<MeasuredData> pProdSentToGrid) {
        this.pProdSentToGrid = pProdSentToGrid;
    }

    public List<MeasuredData> getpProdConsByConsumers() {
        return pProdConsByConsumers;
    }

    public void setpProdConsByConsumers(List<MeasuredData> pProdConsByConsumers) {
        this.pProdConsByConsumers = pProdConsByConsumers;
    }

    public List<MeasuredData> getpProdConsByBat() {
        return pProdConsByBat;
    }

    public void setpProdConsByBat(List<MeasuredData> pProdConsByBat) {
        this.pProdConsByBat = pProdConsByBat;
    }

    public List<MeasuredData> getpBatGlobal() {
        return pBatGlobal;
    }

    public void setpBatGlobal(List<MeasuredData> pBatGlobal) {
        this.pBatGlobal = pBatGlobal;
    }

    public List<MeasuredData> getpBatLoadFromOthers() {
        return pBatLoadFromOthers;
    }

    public void setpBatLoadFromOthers(List<MeasuredData> pBatLoadFromOthers) {
        this.pBatLoadFromOthers = pBatLoadFromOthers;
    }

    public List<MeasuredData> getpBatFedToOthers() {
        return pBatFedToOthers;
    }

    public void setpBatFedToOthers(List<MeasuredData> pBatFedToOthers) {
        this.pBatFedToOthers = pBatFedToOthers;
    }

    public List<MeasuredData> getpBConsoFromGrid() {
        return pBConsoFromGrid;
    }

    public void setpBConsoFromGrid(List<MeasuredData> pBConsoFromGrid) {
        this.pBConsoFromGrid = pBConsoFromGrid;
    }

    public List<MeasuredData> getpBConsoFromBat() {
        return pBConsoFromBat;
    }

    public void setpBConsoFromBat(List<MeasuredData> pBConsoFromBat) {
        this.pBConsoFromBat = pBConsoFromBat;
    }

    public List<MeasuredData> getpBProdConsByConsumers() {
        return pBProdConsByConsumers;
    }

    public void setpBProdConsByConsumers(List<MeasuredData> pBProdConsByConsumers) {
        this.pBProdConsByConsumers = pBProdConsByConsumers;
    }

    public List<MeasuredData> getpBProdConsByBat() {
        return pBProdConsByBat;
    }

    public void setpBProdConsByBat(List<MeasuredData> pBProdConsByBat) {
        this.pBProdConsByBat = pBProdConsByBat;
    }

    public Map<String, Double> getPriceCons() {
        return priceCons;
    }

    public void setPriceCons(Map<String, Double> priceCons) {
        this.priceCons = priceCons;
    }

    public Map<String, Double> getPriceProdSell() {
        return priceProdSell;
    }

    public void setPriceProdSell(Map<String, Double> priceProdSell) {
        this.priceProdSell = priceProdSell;
    }

    public List<MeasuredData> getdProd() {
        return dProd;
    }

    public void setdProd(List<MeasuredData> dProd) {
        this.dProd = dProd;
    }

    public List<MeasuredData> getdCons() {
        return dCons;
    }

    public void setdCons(List<MeasuredData> dCons) {
        this.dCons = dCons;
    }

    public List<MeasuredData> getdStrg() {
        return dStrg;
    }

    public void setdStrg(List<MeasuredData> dStrg) {
        this.dStrg = dStrg;
    }

}
