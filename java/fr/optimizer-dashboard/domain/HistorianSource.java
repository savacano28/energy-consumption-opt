package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.service.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * A Historian.
 */
@Entity
@DiscriminatorValue("HISTORIAN")
public class HistorianSource extends MeasurementSource implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(HistorianSource.class);

    public HistorianSource() {
    }

    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {
        callHistorian(fP);
        return super.getPowersByPeriod(fP);
    }

    private String getTagname() {
        return getParameters().stream().filter(p -> p.getParameterName().equals("tagname")).findFirst().get().getParameterValue();
    }

    private String getServer() {
        return getParameters().stream().filter(p -> p.getParameterName().equals("server")).findFirst().get().getParameterValue();
    }

    public void callHistorian(FluxPeriod fP) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://sensors.ifpen.fr";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        JSONObject params = new JSONObject();
        try {
            params.put("server", getServer());
            params.put("type", "RAW_DATA");
            params.put("tags", new JSONArray(Arrays.asList(getTagname())));
            params.put("start", fP.getStart().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString().replace("T", " "));
            params.put("end", fP.getEnd().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString().replace("T", " "));
            params.put("interval", fP.getStep().toString());
            params.put("numberOfSamples", "");
            params.put("qualifiedValues", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity<String> entity = new HttpEntity<>(params.toString(), headers);
        ResponseDTO response = restTemplate.postForObject(resourceUrl, entity, ResponseDTO.class);


        response.getValues().stream()
            .forEach(v -> super.powerMeasurements.put(LocalDateTime.parse(v.get(0).toString()).atZone(ZoneId.systemDefault()), (Double) ((LinkedHashMap) v.get(1)).get("value")));
    }

   /* public void setInstruction(Instruction instruction) {
    }*/

    @Override
    public String toString() {
        return "Historian{" +
            "id=" + getId() +
            "}";
    }

}

//RestTemplate with get
/*        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://sensors.ifpen.fr";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept",MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl)
            .queryParam("sql", "select timestamp, " + this.sensor_name
                + ".value from ihtrend where "
                + "  timestamp >= '" + fP.getStart().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString().replace("T"," ")
                + "' and timestamp <= '" + fP.getEnd().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString().replace("T"," ")
                + "' and intervalmilliseconds = "+ fP.getStep().toString());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<ResponseDTO> response = restTemplate.exchange(builder.build(false).toUriString(), HttpMethod.GET, entity, ResponseDTO.class);
        response.getBody().getValues().stream()
            .forEach(
                v->super.powerMeasurements.put(LocalDateTime.parse(v.get(0).toString()).atZone(ZoneId.systemDefault()),(Double) v.get(1))
            );*/

