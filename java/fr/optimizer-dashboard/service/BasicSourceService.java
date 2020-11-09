package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.BasicSource;
import fr.ifpen.synergreen.repository.BasicSourceRepository;
import fr.ifpen.synergreen.repository.search.BasicSourceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing BasicSource.
 */
@Service
@Transactional
public class BasicSourceService {

    private final Logger log = LoggerFactory.getLogger(BasicSourceService.class);

    private final BasicSourceRepository basicSourceRepository;

    private final BasicSourceSearchRepository basicSourceSearchRepository;

    public BasicSourceService(BasicSourceRepository basicSourceRepository, BasicSourceSearchRepository basicSourceSearchRepository) {
        this.basicSourceRepository = basicSourceRepository;
        this.basicSourceSearchRepository = basicSourceSearchRepository;
    }

    /**
     * Save a basicSource.
     *
     * @param basicSource the entity to save
     * @return the persisted entity
     */
    public BasicSource save(BasicSource basicSource) {
        log.debug("Request to save BasicSource : {}", basicSource);
        BasicSource result = basicSourceRepository.save(basicSource);
        basicSourceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the basicSources.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BasicSource> findAll() {
        log.debug("Request to get all BasicSources");
        return basicSourceRepository.findAll();
    }

    /**
     * Get one basicSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BasicSource findOne(Long id) {
        log.debug("Request to get BasicSource : {}", id);
        return basicSourceRepository.findOne(id);
    }

    /**
     * Delete the basicSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BasicSource : {}", id);
        basicSourceRepository.delete(id);
        basicSourceSearchRepository.delete(id);
    }

    /**
     * Search for the basicSource corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BasicSource> search(String query) {
        log.debug("Request to search BasicSources for query {}", query);
        return StreamSupport
            .stream(basicSourceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
