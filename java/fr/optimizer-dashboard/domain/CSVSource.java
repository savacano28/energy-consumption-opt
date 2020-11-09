package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * A CSVSource.
 */
@Entity
@DiscriminatorValue("CSV")
public class CSVSource extends MeasurementSource implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CSVSource.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    public CSVSource() {
    }

    public List<MeasuredData> getPowersByPeriod(FluxPeriod fP) {
        loadCSVFile();
        return super.getPowersByPeriod(fP);
    }


   /* public void setInstruction(Instruction instruction) {
    }*/

    private String getFile() {
        return getParameters().stream().filter(p -> p.getParameterName().equals("file")).findFirst().get().getParameterValue();
    }

    public void loadCSVFile() {
        //Definition de variables
        // String file = getModelValueByParameterLabel("file").getStringValue();

        try {
            ClassPathResource resource = new ClassPathResource(getFile());
            InputStream is = resource.getInputStream();
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
            MappingIterator<Map<String, String>> it;
            try {
                it = mapper.readerFor(Map.class).with(schema).readValues(is);
                while (it.hasNext()) {
                    String[] rowAsMap = it.next().values().toString()
                        .replaceAll("[<>\\[\\],-]", "")
                        .replaceAll("/", "-")
                        .split(";");
                    //ZoneOffset.UTC
                    super.getPowerMeasurements().put(ZonedDateTime.parse(rowAsMap[0], formatter.withZone(ZoneId.systemDefault())), Double.parseDouble(rowAsMap[1]));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            is.close();
        } catch (IOException e) {
            log.error("Error : File does not exist");
        }
    }

    @Override
    public String toString() {
        return "CSVSource{" +
            "id=" + getId() +
            "}";
    }

}
