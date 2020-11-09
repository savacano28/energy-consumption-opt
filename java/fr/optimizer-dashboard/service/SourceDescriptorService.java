package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.SourceDescriptor;
import fr.ifpen.synergreen.repository.SourceDescriptorRepository;
import fr.ifpen.synergreen.service.dto.SourceDescriptorDTO;
import fr.ifpen.synergreen.service.mapper.SourceDescriptorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SourceDescriptor.
 */
@Service
@Transactional
public class SourceDescriptorService {

    private final Logger log = LoggerFactory.getLogger(SourceDescriptorService.class);
    private final SourceDescriptorRepository sourceDescriptorRepository;
    private final SourceDescriptorMapper sourceDescriptorMapper;


    public SourceDescriptorService(SourceDescriptorRepository sourceDescriptorRepository,
                                   SourceDescriptorMapper sourceDescriptorMapper) {
        this.sourceDescriptorRepository = sourceDescriptorRepository;
        this.sourceDescriptorMapper = sourceDescriptorMapper;
    }

    /**
     * Save a sourceDescriptor.
     *
     * @param sourceDescriptor the entity to save
     * @return the persisted entity
     */
    public SourceDescriptor save(SourceDescriptor sourceDescriptor) {
        log.debug("Request to save SourceDescriptor : {}", sourceDescriptor);
        SourceDescriptor result = sourceDescriptorRepository.save(sourceDescriptor);
        return result;
    }

    /**
     * Get all the sourceDescriptors.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SourceDescriptorDTO> findAll() {
        log.debug("Request to get all SourceDescriptors");
        return sourceDescriptorRepository.findAll().stream().map(m -> sourceDescriptorMapper.toDTO(m)).collect(Collectors.toList());
    }

    /**
     * Get one sourceDescriptor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SourceDescriptorDTO findOne(Long id) {
        log.debug("Request to get SourceDescriptor : {}", id);
        return sourceDescriptorMapper.toDTO(sourceDescriptorRepository.findOne(id));
    }

    /**
     * Delete the sourceDescriptor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SourceDescriptor : {}", id);
        sourceDescriptorRepository.delete(id);
    }

}
