package fr.ifpen.synergreen.service.dto;


public class BatteryManagementInstructionDTO {

    private String date;
    private Double pProdGlobal;
    private Double deltapProdGlobal;
    private Double lambdaPV;
    private Double pConsoGlobal;
    private Double deltapConsoGlobal;
    private Double lambdapConsoGlobal;
    private Double p_bat;
    private Double soc;

    /*getters and setters*/

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getpProdGlobal() {
        return pProdGlobal;
    }

    public void setpProdGlobal(Double pProdGlobal) {
        this.pProdGlobal = pProdGlobal;
    }

    public Double getDeltapProdGlobal() {
        return deltapProdGlobal;
    }

    public void setDeltapProdGlobal(Double deltapProdGlobal) {
        this.deltapProdGlobal = deltapProdGlobal;
    }

    public Double getLambdaPV() {
        return lambdaPV;
    }

    public void setLambdaPV(Double lambdaPV) {
        this.lambdaPV = lambdaPV;
    }

    public Double getpConsoGlobal() {
        return pConsoGlobal;
    }

    public void setpConsoGlobal(Double pConsoGlobal) {
        this.pConsoGlobal = pConsoGlobal;
    }

    public Double getDeltapConsoGlobal() {
        return deltapConsoGlobal;
    }

    public void setDeltapConsoGlobal(Double deltapConsoGlobal) {
        this.deltapConsoGlobal = deltapConsoGlobal;
    }

    public Double getLambdapConsoGlobal() {
        return lambdapConsoGlobal;
    }

    public void setLambdapConsoGlobal(Double lambdapConsoGlobal) {
        this.lambdapConsoGlobal = lambdapConsoGlobal;
    }

    public Double getP_bat() {
        return p_bat;
    }

    public void setP_bat(Double p_bat) {
        this.p_bat = p_bat;
    }

    public Double getSoc() {
        return soc;
    }

    public void setSoc(Double soc) {
        this.soc = soc;
    }
}
