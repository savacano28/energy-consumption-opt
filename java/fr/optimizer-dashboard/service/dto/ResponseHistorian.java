package fr.ifpen.synergreen.service.dto;


public class ResponseHistorian {

    private String time;
    private Double measure;
    private Double quality;
    private String unit;

    public ResponseHistorian(String time, Double measure) { //ZoneDateTime
        this.time = time;
        this.measure = measure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getMeasure() {
        return measure;
    }

    public void setMeasure(Double measure) {
        this.measure = measure;
    }

    public Double getQuality() {
        return quality;
    }

    public void setQuality(Double quality) {
        this.quality = quality;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
