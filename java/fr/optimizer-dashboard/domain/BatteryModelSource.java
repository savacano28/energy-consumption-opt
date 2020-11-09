package fr.ifpen.synergreen.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A BatteryModelSource.
 */
@Entity
@DiscriminatorValue("BATTERY")
public class BatteryModelSource extends MeasurementSource implements Serializable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    @Transient
    private BufferedWriter out = null;
    @Transient
    private Map<ZonedDateTime, Double> socs = new LinkedHashMap<>();

    public BatteryModelSource() {
        try {
            File testFile = new File("batterySOC.csv");//
            out = new BufferedWriter(new FileWriter(testFile));
            out.write("Timestamp;Initial SOC;Requested Power;Power;finalSOC\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {

        String value = getParameters().stream().filter(p -> p.getParameterName().equals("historicPower")).findFirst().get().getParameterValue();
        value = value.substring(1, value.length() - 1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
        Map<ZonedDateTime, Double> map = new LinkedHashMap<>();

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value
            map.put(ZonedDateTime.parse(entry[0].trim()), Double.parseDouble(entry[1].trim()));          //add them to the hashmap and trim whitespaces
        }

        super.setPowerMeasurements(map);
        return super.getPowersByPeriod(fP);
    }

    /*  public void setInstruction(Instruction i) {
          computeSOC(i);
      }
  */
    private void computeSOC(Double instruction) {
        // Instruction required a given amount of power for a given amount of time
        // This lead to an updated value of SOC and an actual amount of power given or consumed depending on current SOC
        // Convention of sign Pbatt< 0 loading battery, Pbatt>0 unloading battery
       /* Double now = (double) instruction.getEnd().toEpochSecond();
        Double previous = Double.valueOf(instruction.getStart().toEpochSecond());

        //Definition de variables
        Double power;
        Double currentSOC = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("currentSOC")).findFirst().get().getParameterValue());
        Double energy = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("energy")).findFirst().get().getParameterValue());
        power = Math.min(instruction.getCommand(), getMaxPower(currentSOC));
        power = Math.max(power, getMinPower(currentSOC)); //puissance qui fournit la battery

        //Update power value in map parametersValues
        Double finalPower = power;
        getParameters().stream().forEach(p -> {
            if (p.getParameterName().equals("power")) {
                p.setParameterValue(finalPower.toString());
            }
        });
        // Update SOC
        Double oldSOC = currentSOC;
        currentSOC = currentSOC - ((now - previous) * power) / (3600 * energy); // negative power SOC increase, positive power SOC decrease
        Double finalCurrentSOC = currentSOC;
        getParameters().stream().forEach(p -> {
            if (p.getParameterName().equals("currentSOC")) {
                p.setParameterValue(finalCurrentSOC.toString());
            }
        });

        try {
            out.write(previous + ";" + oldSOC + ";" + instruction.getCommand() + ";" + power + ";" + currentSOC + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   powerSOCs.put(instruction.getStart(), new PowerSOC(power, currentSOC));

        getPowerMeasurements().put(instruction.getStart(), power);
        socs.put(instruction.getStart(), currentSOC);*/
    }

    public Double getMaxPower(Double soc) {
        //Definition de variables
        Double maxSOC = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("maxSOC")).findFirst().get().getParameterValue());
        Double maxPower = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("maxPOW")).findFirst().get().getParameterValue());

        if (soc < maxSOC) {
            return (maxPower * soc) / maxSOC;
        } else
            return maxPower;
    }

    private Double getMinPower(Double soc) {
        //Definition de variables
        Double minSOC = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("minSOC")).findFirst().get().getParameterValue());
        Double minPower = Double.valueOf(getParameters().stream().filter(p -> p.getParameterName().equals("minPOW")).findFirst().get().getParameterValue());

        if (soc > minSOC) {
            return minPower * (1 - soc) / (1 - minSOC);
        } else
            return minPower;
    }


    @Override
    public String toString() {
        return "BatteryModelSource{" +
            "id=" + getId() +
            "}";
    }

}
