package fr.ifpen.synergreen.domain.enumeration;


public enum SourceType {
    CSV("records", "csv_records"),

    HISTORIAN("records", "historian_records"),

    BATTERY_MODEL("model", "battery_model"),

    BASIC("interne", "basic_source"),

    PV_MODEL("model", "pv_model");

    private String type;
    private String label;

    SourceType(String label, String type) {
        this.type = type;
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
