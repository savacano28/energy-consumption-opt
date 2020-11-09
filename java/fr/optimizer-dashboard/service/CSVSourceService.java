package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.CSVSource;
import fr.ifpen.synergreen.repository.CSVSourceRepository;
import fr.ifpen.synergreen.repository.search.CSVSourceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing CSVSource.
 */
@Service
@Transactional
public class CSVSourceService {

    private final Logger log = LoggerFactory.getLogger(CSVSourceService.class);

    private final CSVSourceRepository cSVSourceRepository;

    private final CSVSourceSearchRepository cSVSourceSearchRepository;

    public CSVSourceService(CSVSourceRepository cSVSourceRepository, CSVSourceSearchRepository cSVSourceSearchRepository) {
        this.cSVSourceRepository = cSVSourceRepository;
        this.cSVSourceSearchRepository = cSVSourceSearchRepository;
    }

    /**
     * Save a cSVSource.
     *
     * @param cSVSource the entity to save
     * @return the persisted entity
     */
    public CSVSource save(CSVSource cSVSource) {
        log.debug("Request to save CSVSource : {}", cSVSource);
        // cSVSourceRepository.save(cSVSource);
        CSVSource result = cSVSourceRepository.save(cSVSource);
        cSVSourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cSVSources.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CSVSource> findAll() {
        log.debug("Request to get all CSVSources");
        return cSVSourceRepository.findAll();
    }

    /**
     * Get one cSVSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CSVSource findOne(Long id) {
        log.debug("Request to get CSVSource : {}", id);
        return cSVSourceRepository.findOne(id);
    }

    /**
     * Delete the cSVSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CSVSource : {}", id);
        cSVSourceRepository.delete(id);
        cSVSourceSearchRepository.delete(id);
    }

    /**
     * Search for the cSVSource corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CSVSource> search(String query) {
        log.debug("Request to search CSVSources for query {}", query);
        return StreamSupport
            .stream(cSVSourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Get one cSVSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CSVSource findOneByEnergyElementId(Long id) {
        log.debug("Request to get CSVSource : {}", id);
        return cSVSourceRepository.findOne(id);
    }

}
