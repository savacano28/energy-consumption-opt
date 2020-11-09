package fr.ifpen.synergreen.service;


import fr.ifpen.synergreen.domain.HistorianSource;
import fr.ifpen.synergreen.domain.MeasuredData;
import fr.ifpen.synergreen.repository.HistorianRepository;
import fr.ifpen.synergreen.repository.search.HistorianSearchRepository;
import fr.ifpen.synergreen.service.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Service Implementation for managing Historian.
 */
@Service
@Transactional
public class HistorianService {

    private static final Logger log = LoggerFactory.getLogger(HistorianService.class);

    private final HistorianRepository historianRepository;

    private final HistorianSearchRepository historianSearchRepository;


    public HistorianService(HistorianRepository historianRepository, HistorianSearchRepository historianSearchRepository) {
        this.historianRepository = historianRepository;
        this.historianSearchRepository = historianSearchRepository;
    }

    public static List<List<Object>> getPowersFromHistorian(String sensor, String start, String end, String interval) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://sensors.ifpen.fr";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);

        JSONObject params = new JSONObject();
        try {
            params.put("server", "ISNTS35-N");
            params.put("type", "RAW_DATA");
            params.put("tags", new JSONArray(Arrays.asList(sensor)));
            params.put("start", start);
            params.put("end", end);
            params.put("interval", interval);
            params.put("numberOfSamples", "1");
            params.put("qualifiedValues", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity<String> entity = new HttpEntity<>(params.toString(), headers);
        ResponseDTO response = restTemplate.postForObject("http://sensors.ifpen.fr", entity, ResponseDTO.class);

        return response.getValues();
    }

    /**
     * Save a historian.
     *
     * @param historianSource the entity to save
     * @return the persisted entity
     */
    public HistorianSource save(HistorianSource historianSource) {
        log.debug("Request to save Historian : {}", historianSource);
        HistorianSource result = historianRepository.save(historianSource);
        historianSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the historians.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<HistorianSource> findAll() {
        log.debug("Request to get all Historians");
        return historianRepository.findAll();
    }

    /**
     * Get one historian by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HistorianSource findOne(Long id) {
        log.debug("Request to get Historian : {}", id);
        return historianRepository.findOne(id);
    }

  /*  *//**
     * Search for the historian corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     *//*
    @Transactional(readOnly = true)
    public List<Historian> search(String query) {
        log.debug("Request to search Historians for query {}", query);
        return StreamSupport
            .stream(historianSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }*/

    /**
     * Delete the historian by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Historian : {}", id);
        historianRepository.delete(id);
        historianSearchRepository.delete(id);
    }

    /**
     * Get one cSVSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HistorianSource findOneByEnergyElementId(Long id) {
        log.debug("Request to get BatterySource : {}", id);
        return historianRepository.findOne(id);
    }

    public MeasuredData getPower(Long id, ZonedDateTime t) {

        return new MeasuredData();
    }


}
