package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.MeasurementSource;
import fr.ifpen.synergreen.repository.MeasurementSourceRepository;
import fr.ifpen.synergreen.service.dto.MeasurementSourceDTO;
import fr.ifpen.synergreen.service.mapper.MeasurementSourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MeasurementSource.
 */
@Service
@Transactional
public class MeasurementSourceService {

    private final Logger log = LoggerFactory.getLogger(MeasurementSourceService.class);
    private final MeasurementSourceRepository measurementSourceRepository;
    private final MeasurementSourceMapper measurementSourceMapper;

    public MeasurementSourceService(MeasurementSourceRepository measurementSourceRepository,
                                    MeasurementSourceMapper measurementSourceMapper) {
        this.measurementSourceRepository = measurementSourceRepository;
        this.measurementSourceMapper = measurementSourceMapper;
    }

    /**
     * Save a measurementSource.
     *
     * @param measurementSource the entity to save
     * @return the persisted entity
     */
    public MeasurementSource save(MeasurementSource measurementSource) {
        log.debug("Request to save MeasurementSource : {}", measurementSource);
        MeasurementSource result = measurementSourceRepository.save(measurementSource);
        return result;
    }

    /**
     * Get all the measurementSources.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MeasurementSourceDTO> findAll() {
        log.debug("Request to get all MeasurementSources");
        return measurementSourceRepository.findAll().stream().map(m -> measurementSourceMapper.toDTO(m)).collect(Collectors.toList());
    }

    /**
     * Get one measurementSource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MeasurementSourceDTO findOne(Long id) {
        log.debug("Request to get MeasurementSource : {}", id);
        return measurementSourceMapper.toDTO(measurementSourceRepository.findOne(id));
    }

    /**
     * Delete the measurementSource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasurementSource : {}", id);
        measurementSourceRepository.delete(id);
    }

}
