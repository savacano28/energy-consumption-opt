package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.BatteryModelSource;
import fr.ifpen.synergreen.domain.MeasuredData;
import fr.ifpen.synergreen.repository.BatteryModelSourceRepository;
import fr.ifpen.synergreen.repository.search.BatteryModelSourceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing BatteryModelSource.
 */
@Service
@Transactional
public class BatteryModelSourceService {

    private final Logger log = LoggerFactory.getLogger(BatteryModelSourceService.class);

    private final BatteryModelSourceRepository batteryModelSourceRepository;

    private final BatteryModelSourceSearchRepository batteryModelSourceSearchRepository;

    public BatteryModelSourceService(BatteryModelSourceRepository batteryModelSourceRepository, BatteryModelSourceSearchRepository batteryModelSourceSearchRepository) {
        this.batteryModelSourceRepository = batteryModelSourceRepository;
        this.batteryModelSourceSearchRepository = batteryModelSourceSearchRepository;
    }

    /**
     * Save a batteryModelSource.
     *
     * @param batteryModelSource the entity to save
     * @return the persisted entity
     */
    public BatteryModelSource save(BatteryModelSource batteryModelSource) {
        log.debug("Request to save BatteryModelSource : {}", batteryModelSource);
        BatteryModelSource result = batteryModelSourceRepository.save(batteryModelSource);
        batteryModelSourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the batteryModelSources.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryModelSource> findAll() {
        log.debug("Request to get all BatteryModelSources");
        return batteryModelSourceRepository.findAll();
    }

    /**
     * Get one batteryModelSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BatteryModelSource findOne(Long id) {
        log.debug("Request to get BatteryModelSource : {}", id);
        return batteryModelSourceRepository.findOne(id);
    }

    /**
     * Delete the batteryModelSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BatteryModelSource : {}", id);
        batteryModelSourceRepository.delete(id);
        batteryModelSourceSearchRepository.delete(id);
    }

    /**
     * Search for the batteryModelSource corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryModelSource> search(String query) {
        log.debug("Request to search BatteryModelSources for query {}", query);
        return StreamSupport
            .stream(batteryModelSourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BatteryModelSource findOneByEnergyElementId(Long id) {
        log.debug("Request to get BatterySource : {}", id);
        return batteryModelSourceRepository.findOne(id);
    }

    /**
     * Get one cSVSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MeasuredData getPower(Long id, ZonedDateTime t) {
        return new MeasuredData();
    }

}
