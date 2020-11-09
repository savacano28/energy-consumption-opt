package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.MeasuredData;
import fr.ifpen.synergreen.domain.PVModelSource;
import fr.ifpen.synergreen.repository.PVModelSourceRepository;
import fr.ifpen.synergreen.repository.search.PVModelSourceSearchRepository;
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
 * Service Implementation for managing PVModelSource.
 */
@Service
@Transactional
public class PVModelSourceService {

    private final Logger log = LoggerFactory.getLogger(PVModelSourceService.class);

    private final PVModelSourceRepository pVModelSourceRepository;

    private final PVModelSourceSearchRepository pVModelSourceSearchRepository;

    public PVModelSourceService(PVModelSourceRepository pVModelSourceRepository, PVModelSourceSearchRepository pVModelSourceSearchRepository) {
        this.pVModelSourceRepository = pVModelSourceRepository;
        this.pVModelSourceSearchRepository = pVModelSourceSearchRepository;
    }

    /**
     * Save a pVModelSource.
     *
     * @param pVModelSource the entity to save
     * @return the persisted entity
     */
    public PVModelSource save(PVModelSource pVModelSource) {
        log.debug("Request to save PVModelSource : {}", pVModelSource);
        PVModelSource result = pVModelSourceRepository.save(pVModelSource);
        pVModelSourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the pVModelSources.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PVModelSource> findAll() {
        log.debug("Request to get all PVModelSources");
        return pVModelSourceRepository.findAll();
    }

    /**
     * Get one pVModelSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PVModelSource findOne(Long id) {
        log.debug("Request to get PVModelSource : {}", id);
        return pVModelSourceRepository.findOne(id);
    }

    /**
     * Delete the pVModelSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PVModelSource : {}", id);
        pVModelSourceRepository.delete(id);
        pVModelSourceSearchRepository.delete(id);
    }

    /**
     * Search for the pVModelSource corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PVModelSource> search(String query) {
        log.debug("Request to search PVModelSources for query {}", query);
        return StreamSupport
            .stream(pVModelSourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Get one cSVSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PVModelSource findOneByEnergyElementId(Long id) {
        log.debug("Request to get CSVSource : {}", id);
        return pVModelSourceRepository.findOne(id);
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
