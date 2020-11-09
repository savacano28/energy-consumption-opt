package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.EnergySite;
import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.repository.EnergySiteRepository;
import fr.ifpen.synergreen.repository.FluxGroupRepository;
import fr.ifpen.synergreen.repository.FluxTopologyRepository;
import fr.ifpen.synergreen.repository.search.EnergySiteSearchRepository;
import fr.ifpen.synergreen.service.dto.EnergySiteDTO;
import fr.ifpen.synergreen.service.mapper.EnergySiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EnergySite.
 */
@Service
@Transactional
public class EnergySiteService {

    private final Logger log = LoggerFactory.getLogger(EnergySiteService.class);

    private final EnergySiteRepository energySiteRepository;

    private final EnergySiteSearchRepository energySiteSearchRepository;
    private final FluxTopologyRepository fluxTopologyRepository;
    private final FluxGroupRepository fluxGroupRepository;
    private final EnergySiteMapper energySiteMapper;

    public EnergySiteService(EnergySiteMapper energySiteMapper, FluxGroupRepository fluxGroupRepository, FluxTopologyRepository fluxTopologyRepository, EnergySiteRepository energySiteRepository, EnergySiteSearchRepository energySiteSearchRepository) {
        this.energySiteRepository = energySiteRepository;
        this.energySiteSearchRepository = energySiteSearchRepository;
        this.fluxTopologyRepository = fluxTopologyRepository;
        this.fluxGroupRepository = fluxGroupRepository;
        this.energySiteMapper = energySiteMapper;
    }

    /**
     * Save a energySite.
     *
     * @param energySite the entity to save
     * @return the persisted entity
     */
    public EnergySiteDTO save(EnergySiteDTO energySite) {
        log.debug("Request to save EnergySite : {}", energySite);
        return energySiteMapper.toDtoWithOptimizations(energySiteRepository.save(energySiteMapper.toEntity(energySite)));
    }

    /**
     * Get all the energySites.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergySiteDTO> findAll() {
        log.debug("Request to get all EnergySites");
        return energySiteRepository.findAll().stream().map(c -> energySiteMapper.toDtoWithOptimizations(c)).collect(Collectors.toList());
    }


    /**
     * Get one energySite by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EnergySiteDTO findOne(Long id) {
        log.debug("Request to get EnergySite : {}", id);
        return energySiteMapper.toDtoWithOptimizations(energySiteRepository.findOne(id));
    }

    @Transactional(readOnly = true)
    public List<EnergySiteDTO> findAllPilotables() {
        log.debug("Request to get all FluxTopologies");
        return energySiteRepository.findAll()
            .stream()
            .filter(s ->
                (s.getFluxTopologies()
                    .stream()
                    .filter(t ->
                        t.getChildren()
                            .stream()
                            .map(FluxNode::getAllChildren)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
                            .stream()
                            .anyMatch(
                                e -> e.getType().equals(FluxNodeType.BATTERY)))
                    .collect(Collectors.toList())).size() > 0)
            .map(energySiteMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Delete the energySite by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EnergySite : {}", id);
        energySiteRepository.delete(id);
        energySiteSearchRepository.delete(id);
    }

    /**
     * Search for the energySite corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergySite> search(String query) {
        log.debug("Request to search EnergySites for query {}", query);
        return StreamSupport
            .stream(energySiteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
