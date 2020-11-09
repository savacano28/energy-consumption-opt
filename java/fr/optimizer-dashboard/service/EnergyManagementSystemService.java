package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.*;
import fr.ifpen.synergreen.repository.EnergyManagementSystemRepository;
import fr.ifpen.synergreen.repository.search.EnergyManagementSystemSearchRepository;
import fr.ifpen.synergreen.service.dto.BatteryManagementResultDTO;
import fr.ifpen.synergreen.service.dto.BatteryManagementSourceDTO;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import fr.ifpen.synergreen.service.mapper.BatteryManagementResultMapper;
import fr.ifpen.synergreen.service.mapper.FluxTopologyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.getCurrentTime;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing EnergyManagementSystem.
 */
@Service
@Transactional
public class EnergyManagementSystemService {

    private final Logger log = LoggerFactory.getLogger(EnergyManagementSystemService.class);

    private final EnergyManagementSystemRepository energyManagementSystemRepository;
    private final EnergyManagementSystemSearchRepository energyManagementSystemSearchRepository;
    private final FluxTopologyService fluxTopologyService;
    private final FluxTopologyMapper fluxTopologyMapper;
    private final EnergyProviderService energyProviderService;
    private final BatteryManagementService batteryManagementService;
    private final BatteryManagementResultMapper batteryManagementResultMapper;


    public EnergyManagementSystemService(EnergyManagementSystemRepository energyManagementSystemRepository,
                                         EnergyManagementSystemSearchRepository energyManagementSystemSearchRepository,
                                         FluxTopologyService fluxTopologyService,
                                         FluxTopologyMapper fluxTopologyMapper,
                                         EnergyProviderService energyProviderService,
                                         BatteryManagementService batteryManagementService,
                                         BatteryManagementResultMapper batteryManagementResultMapper) {

        this.energyManagementSystemRepository = energyManagementSystemRepository;
        this.energyManagementSystemSearchRepository = energyManagementSystemSearchRepository;
        this.fluxTopologyService = fluxTopologyService;
        this.fluxTopologyMapper = fluxTopologyMapper;
        this.energyProviderService = energyProviderService;
        this.batteryManagementService = batteryManagementService;
        this.batteryManagementResultMapper = batteryManagementResultMapper;
    }

    /**
     * Save a energyManagementSystem.
     *
     * @param energyManagementSystem the entity to save
     * @return the persisted entity
     */
    public EnergyManagementSystem save(EnergyManagementSystem energyManagementSystem) {
        log.debug("Request to save EnergyManagementSystem : {}", energyManagementSystem);
        EnergyManagementSystem result = energyManagementSystemRepository.save(energyManagementSystem);
        energyManagementSystemSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the energyManagementSystems.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergyManagementSystem> findAll() {
        log.debug("Request to get all EnergyManagementSystems");
        return energyManagementSystemRepository.findAll();
    }

    /**
     * Get one energyManagementSystem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EnergyManagementSystem findOne(Long id) {
        log.debug("Request to get EnergyManagementSystem : {}", id);
        return energyManagementSystemRepository.findOne(id);
    }

    /**
     * Delete the energyManagementSystem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EnergyManagementSystem : {}", id);
        energyManagementSystemRepository.delete(id);
        energyManagementSystemSearchRepository.delete(id);
    }

    /**
     * Search for the energyManagementSystem corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EnergyManagementSystem> search(String query) {
        log.debug("Request to search EnergyManagementSystems for query {}", query);
        return StreamSupport
            .stream(energyManagementSystemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


    /**
     * Monitoring a configuration
     *
     * @return the list of balance data
     */
    @Transactional(readOnly = true)
    public List<StateFluxNodeDTO> monitoring(Long id, ZonedDateTime start, ZonedDateTime end, Long step) {
        log.debug("Request to monitoring EnergyManagementSystems {}", id);
        FluxTopology fluxTopology = fluxTopologyService.findOne(id); //TODO pass topology in params, pass a period?
        FluxPeriod fluxPeriod = new FluxPeriod(start, end, step);
        return fluxTopology.getStateSummary(fluxPeriod);
    }



















   /* private void computeStorageInstruction(FluxTopology fluxTopology, BalanceData bd, Long step) { //we need save all instructions
        fluxTopology.getEnergyElement(FluxNodeType.BATTERY).stream()
            .forEach(x -> { //every battery
                currentInstruction.setStart(bd.getInstant().plus(step, SECONDS)); //verifier
                currentInstruction.setEnd(bd.getInstant().plus(2 * step, SECONDS));
                currentInstruction.setCommand(surplusStrategyLive(bd)); // FIXME compute command with BalanceData with surplus algorithm (see Esdras code)
                x.setInstruction(currentInstruction);
            });
    }*/

 /*   public Instruction getCurrentInstruction() {
        return currentInstruction;
    }*/

   /* public void setCurrentInstruction(Instruction currentInstruction) {
        this.currentInstruction = currentInstruction;
    }*/


    /**
     * Controlling a Storage Node -- Real DB properties
     *
     * @return the id for the new control run
     */
    public BatteryManagementResultDTO computeNewInstructions(Long clientId,
                                                             Long fluxTopologyId,
                                                             Long batteryId,
                                                             Double SoC0) {

        log.debug("REST request to get the last control run status and results.");
        ZonedDateTime currentTime = getCurrentTime().truncatedTo(ChronoUnit.SECONDS);;

        BatteryManagementResult batteryManagementResult;
        BatteryManagementResultDTO batteryManagementResultDTO;

        try {
            log.debug("Request to get Topology");
            FluxTopology fluxTopology = this.fluxTopologyService.findOne(fluxTopologyId);
            if (fluxTopology == null) {
                batteryManagementResult = new BatteryManagementResult(null, "format invalid",
                    "Topology not existing.");
                return batteryManagementResultMapper.toDto(batteryManagementResult);
            }

            if (fluxTopology.getEnergyProvider() != null) {
                log.debug("Request to get Energy Provider");
                EnergyProvider energyProvider = fluxTopology.getEnergyProvider();
                if (!fluxTopology.getChildren().isEmpty()) {
                    log.debug("Request to get Battery Unit to control");
                    FluxNode battery = fluxTopology.findOneChild(batteryId);
                    if (battery == null) {
                        log.info("Battery of id {} device not found in topology {}.", batteryId, fluxTopologyId);
                        batteryManagementResult = new BatteryManagementResult(null, "terminated", "Battery device not found in flux topology.");

                    } else {
                        // Compute new instructions based on the physical and algorithmical properties stored in the DDB
                        batteryManagementResultDTO =
                            batteryManagementService.computeNewInstructions(currentTime, clientId,
                                energyProvider, fluxTopology, battery, SoC0);

//                        BatteryManager run = batteryManagementRunService.findOneSimple(batteryManagementResultDTO.getRunId());
                        BatteryManager batteryManager = fluxTopology.getBatteryManager();
                        fluxTopology = fluxTopologyService.update(fluxTopology);
                        return batteryManagementResultDTO;
                    }
                } else {
                    log.info("Topology {} has no children.", fluxTopologyId);
                    batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology has no children.");
                }
            } else {
                log.info("Topology {} has no energy provider found.", fluxTopologyId);
                batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology has no children.");
            }
        } catch (Exception e) {
            log.info("Topology {} not existing.", fluxTopologyId);
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology not existing.");
        }

        return batteryManagementResultMapper.toDto(batteryManagementResult);
    }


    /**
     * Controlling a Storage Node -- Real DB properties
     *
     * @return the id for the new control run
     */
    public BatteryManagementResultDTO computeNewInstructions(Long clientId,
                                                             Long fluxTopologyId,
                                                             Long batteryId,
                                                             ZonedDateTime measureDate,
                                                             Double pBat0,
                                                             Double SoC0) {

        log.debug("REST request to get the last control run status and results.");
        ZonedDateTime currentTime = getCurrentTime().truncatedTo(ChronoUnit.SECONDS);;

        BatteryManagementResult batteryManagementResult;
        BatteryManagementResultDTO batteryManagementResultDTO;

        try {
            log.debug("Request to get Topology");
            FluxTopology fluxTopology = fluxTopologyService.findOne(fluxTopologyId);
            if (fluxTopology == null) {
                batteryManagementResult = new BatteryManagementResult(null, "terminated",
                    "Flux topology not existing.");
                return batteryManagementResultMapper.toDto(batteryManagementResult);
            }

            if (fluxTopology.getEnergyProvider() != null) {
                log.debug("Request to get Energy Provider");
                EnergyProvider energyProvider = fluxTopology.getEnergyProvider();
                if (!fluxTopology.getChildren().isEmpty()) {
                    log.debug("Request to get Battery Unit to control");
                    FluxNode battery = fluxTopology.findOneChild(batteryId);
                    if (battery == null) {
                        log.info("Battery device of id {} not found in topology {}.", batteryId, fluxTopologyId);
                        batteryManagementResult = new BatteryManagementResult(null, "terminated", "Battery device not found in flux topology.");
                        return batteryManagementResultMapper.toDto(batteryManagementResult);

                    } else {
                        log.info("Battery device {} was found.", batteryId);
                        // Store pBat0 into the DB as a new MeasureData for the battery device
                        String historicalPower = battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("historicalPower")).findFirst().get().getParameterValue();
                        historicalPower = historicalPower.replace("}", ",") + measureDate + "=" + pBat0 + "}";
                        String historicalSoc = battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("historicalSoc")).findFirst().get().getParameterValue();
                        historicalSoc = historicalSoc.replace("}", ",") + measureDate + "=" + SoC0 + "}";
                        battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("historicalPower")).findFirst().get().setParameterValue(historicalPower);
                        battery.getMeasurementSource().getParameters().stream().filter(p -> p.getParameterLabel().equals("historicalSoc")).findFirst().get().setParameterValue(historicalSoc);

                        // Compute new instructions based on the physical and algorithmical properties stored in the DDB
                        batteryManagementResultDTO =
                            batteryManagementService.computeNewInstructions(currentTime, clientId, energyProvider,
                                fluxTopology, battery, SoC0);
                        fluxTopologyService.update(fluxTopology);
                        return batteryManagementResultDTO;
                    }
                } else {
                    log.info("Topology {} has no children.", fluxTopologyId);
                    batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology has no children.");
                    return batteryManagementResultMapper.toDto(batteryManagementResult);
                }
            } else {
                log.info("Topology {} has no energy provider found.", fluxTopologyId);
                batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology has no children.");
                return batteryManagementResultMapper.toDto(batteryManagementResult);
            }
        } catch (Exception e) {
            log.debug("Unidentified internal error in computeNewInstructions.");
            log.debug("Exception: ", e.getMessage(), e.getCause());
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Internal error while trying to compute new instructions.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }
    }


    /**
     * Controlling a Storage Node -- Test mode with JSON Input
     *
     * @return the id for the new control run
     */
    /* Method allowing for a JSON Input to be passed as body to the request */
    public BatteryManagementResultDTO computeNewInstructionsWithJson(Long clientId,
                                                                     Long fluxTopologyId,
                                                                     Long batteryId,
                                                                     BatteryManagementSourceDTO BatteryManagementSourceDTO) {

        log.debug("REST request to get the last control run status and results.");
        ZonedDateTime currentTime = getCurrentTime().truncatedTo(ChronoUnit.SECONDS);;

        BatteryManagementResult batteryManagementResult;
        BatteryManagementResultDTO batteryManagementResultDTO;

        try {
            log.debug("Request to get Topology");
            FluxTopology fluxTopology = this.fluxTopologyService.findOne(fluxTopologyId);
            if (fluxTopology == null) {
                batteryManagementResult = new BatteryManagementResult(null, "format invalid",
                    "Topology not existing.");
                return batteryManagementResultMapper.toDto(batteryManagementResult);
            }

            batteryManagementResultDTO =
                batteryManagementService.computeNewInstructionsWithJson(currentTime, clientId, fluxTopology,
                    batteryId, BatteryManagementSourceDTO);
            return batteryManagementResultDTO;
        } catch (Exception e) {
            log.info("Topology not existing.");
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Flux topology not existing.");
        }

        return batteryManagementResultMapper.toDto(batteryManagementResult);
    }


    /**
     * Controlling a Storage Node
     *
     * @return the status and the result of the controlling every step if available
     */
    public BatteryManagementResultDTO getInstructions(Long clientId,
                                                      Long fluxTopologyId,
                                                      Long batteryId,
                                                      Long runId) {
        log.debug("REST request to get the last control run status and results for query {}", runId);
        BatteryManagementResult batteryManagementResult;
        FluxTopology fluxTopology = fluxTopologyService.findOne(fluxTopologyId);
        if (fluxTopology == null) {
            batteryManagementResult = new BatteryManagementResult(runId, "format invalid",
                "Topology not existing.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }

        return batteryManagementService.getInstructions(clientId, fluxTopology, batteryId, runId);
    }


    /**
     * Controlling a Storage Node
     *
     * @return the status and the result of the controlling every step if available
     */
    public BatteryManagementResultDTO getInstructions(Long clientId,
                                                      Long fluxTopologyId,
                                                      Long batteryId) {
        log.debug("REST request to get the last control run status and results for query");
        BatteryManagementResult batteryManagementResult;
        FluxTopology fluxTopology = fluxTopologyService.findOne(fluxTopologyId);
        if (fluxTopology == null) {
            batteryManagementResult = new BatteryManagementResult(null, "format invalid",
                "Topology not existing.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }

        return batteryManagementService.getInstructions(clientId, fluxTopology, batteryId);
    }
}
