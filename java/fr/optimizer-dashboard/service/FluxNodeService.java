package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.repository.BatteryModelSourceRepository;
import fr.ifpen.synergreen.repository.FluxNodeRepository;
import fr.ifpen.synergreen.service.dto.FluxNodeDTO;
import fr.ifpen.synergreen.service.mapper.FluxNodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing FluxNode.
 */
@Service
@Transactional
public class FluxNodeService {

    private final Logger log = LoggerFactory.getLogger(FluxNodeService.class);

    private final FluxNodeRepository fluxNodeRepository;
    private final BatteryModelSourceRepository batteryModelSourceRepository;


    private final MeasurementSourceService measurementSourceService;
    private final CSVSourceService csvSourceService;
    private final PVModelSourceService pvModelSourceService;
    private final BatteryModelSourceService batteryModelSourceService;
    private final HistorianService historianService;
    private final FluxNodeMapper fluxNodeMapper;


    public FluxNodeService(FluxNodeRepository fluxNodeRepository,
                           MeasurementSourceService measurementSourceService,
                           CSVSourceService csvSourceService,
                           PVModelSourceService pvModelSourceService,
                           BatteryModelSourceService batteryModelSourceService,
                           HistorianService historianService,
                           FluxNodeMapper fluxNodeMapper,
                           BatteryModelSourceRepository batteryModelSourceRepository
    ) {
        this.fluxNodeRepository = fluxNodeRepository;
        this.measurementSourceService = measurementSourceService;
        this.csvSourceService = csvSourceService;
        this.pvModelSourceService = pvModelSourceService;
        this.batteryModelSourceService = batteryModelSourceService;
        this.historianService = historianService;
        this.fluxNodeMapper = fluxNodeMapper;
        this.batteryModelSourceRepository = batteryModelSourceRepository;
    }

    /**
     * Save a fluxNode.
     *
     * @param fluxNode the entity to save
     * @return the persisted entity
     */
    public FluxNode save(FluxNode fluxNode) {
        log.debug("Request to save FluxNode : {}", fluxNode);
        FluxNode result = fluxNodeRepository.save(fluxNode);
        return result;
    }

    /**
     * Get all the fluxNodes.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FluxNodeDTO> findAll() {
        log.debug("Request to get all FluxNodes");
        return fluxNodeRepository.findAll().stream().map(f -> fluxNodeMapper.toDto(f)).collect(Collectors.toList());
    }

    /**
     * Get one fluxNode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FluxNodeDTO findOneDTO(Long id) {
        log.debug("Request to get FluxNode : {}", id);
        return fluxNodeMapper.toDto(fluxNodeRepository.findOne(id));
    }

    /**
     * Get one fluxNode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FluxNode findOne(Long id) {
        log.debug("Request to get FluxNode : {}", id);
        return fluxNodeRepository.findOne(id);
    }

    /**
     * Get one fluxNode by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<FluxNodeDTO> findEnergyElements() {
        log.debug("Request to get all element energy : {}");
        return fluxNodeRepository.findEnergyElements().stream().map(c -> fluxNodeMapper.toDto(c)).collect(Collectors.toList());
    }

    /**
     * Get one fluxNode by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<FluxNodeDTO> findFluxGroups() {
        log.debug("Request to get all flux groups : {}");
        return fluxNodeRepository.findFluxGroups().stream().map(fluxNodeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Delete the fluxNode by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FluxNode : {}", id);
        fluxNodeRepository.delete(id);
    }

}
