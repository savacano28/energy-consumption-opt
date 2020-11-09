package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.EnergyProvider;
import fr.ifpen.synergreen.repository.EnergyProviderRepository;
import fr.ifpen.synergreen.repository.search.EnergyProviderSearchRepository;
import fr.ifpen.synergreen.service.dto.EnergyProviderDTO;
import fr.ifpen.synergreen.service.mapper.EnergyProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EnergyProvider.
 */
@Service
@Transactional
public class EnergyProviderService {

    private final Logger log = LoggerFactory.getLogger(EnergyProviderService.class);
    private final EnergyProviderRepository energyProviderRepository;
    private final EnergyProviderMapper energyProviderMapper;
    private final EnergyProviderSearchRepository energyProviderSearchRepository;

    public EnergyProviderService(EnergyProviderRepository energyProviderRepository, EnergyProviderSearchRepository energyProviderSearchRepository, EnergyProviderMapper energyProviderMapper) {
        this.energyProviderRepository = energyProviderRepository;
        this.energyProviderSearchRepository = energyProviderSearchRepository;
        this.energyProviderMapper = energyProviderMapper;
    }

    /**
     * Save a energyProvider.
     *
     * @param energyProvider the entity to save
     * @return the persisted entity
     */
    public EnergyProvider save(EnergyProvider energyProvider) {
        log.debug("Request to save EnergyProvider : {}", energyProvider);
        EnergyProvider result = energyProviderRepository.save(energyProvider);
        energyProviderSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the energyProviders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergyProviderDTO> findAll() {
        log.debug("Request to get all EnergyProviders");
        return energyProviderRepository.findAll().stream().map(energyProviderMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one energyProvider by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EnergyProviderDTO findOneDTO(Long id) {
        log.debug("Request to get EnergyProvider : {}", id);
        return energyProviderMapper.toDto(energyProviderRepository.findOne(id));
    }

    /**
     * Get one energyProvider by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EnergyProvider findOne(Long id) {
        log.debug("Request to get EnergyProvider : {}", id);
        return energyProviderRepository.findOne(id);
    }

    /**
     * Delete the energyProvider by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EnergyProvider : {}", id);
        energyProviderRepository.delete(id);
        energyProviderSearchRepository.delete(id);
    }

    /**
     * Search for the energyProvider corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergyProvider> search(String query) {
        log.debug("Request to search EnergyProviders for query {}", query);
        return StreamSupport
            .stream(energyProviderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
