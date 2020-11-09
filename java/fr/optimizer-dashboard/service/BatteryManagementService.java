package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.config.ApplicationProperties;
import fr.ifpen.synergreen.domain.*;
import fr.ifpen.synergreen.service.dto.BatteryManagementResultDTO;
import fr.ifpen.synergreen.service.dto.BatteryManagementSourceDTO;
import fr.ifpen.synergreen.service.mapper.BatteryManagementResultMapper;
import fr.ifpen.synergreen.service.mapper.BatteryManagementSourceMapper;
import org.elasticsearch.common.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.ZonedDateTime;

@Service
@Transactional
public class BatteryManagementService {

    private final Logger log = LoggerFactory.getLogger(BatteryManagementService.class);
    private final FluxTopologyService fluxTopologyService;
    private final BatteryManagementRunService batteryManagementRunService;
    private final BatteryManagementInstructionService batteryManagementInstructionService;
    private final BatteryManagementResultMapper batteryManagementResultMapper;
    private final BatteryManagementSourceMapper batteryManagementSourceMapper;
    private final BatteryManagerService batteryManagerService;
    private final ApplicationProperties applicationProperties;

    @Inject
    public BatteryManagementService(FluxTopologyService fluxTopologyService,
                                    BatteryManagementRunService batteryManagementRunService,
                                    BatteryManagementInstructionService batteryManagementInstructionService,
                                    BatteryManagementResultMapper batteryManagementResultMapper,
                                    BatteryManagementSourceMapper batteryManagementSourceMapper,
                                    BatteryManagerService batteryManagerService,
                                    ApplicationProperties applicationProperties) {
        this.fluxTopologyService = fluxTopologyService;
        this.batteryManagementRunService = batteryManagementRunService;
        this.batteryManagementInstructionService = batteryManagementInstructionService;
        this.batteryManagementResultMapper = batteryManagementResultMapper;
        this.batteryManagementSourceMapper = batteryManagementSourceMapper;
        this.batteryManagerService = batteryManagerService;
        this.applicationProperties = applicationProperties;
    }

    public BatteryManagementRunService getBatteryManagementRunService() {
        return batteryManagementRunService;
    }

    public BatteryManagementInstructionService getBatteryManagementInstructionService() {
        return batteryManagementInstructionService;
    }

    public BatteryManagementResultMapper getBatteryManagementResultMapper() {
        return batteryManagementResultMapper;
    }

    public BatteryManagementSourceMapper getBatteryManagementSourceMapper() {
        return batteryManagementSourceMapper;
    }

    public BatteryManagerService getBatteryManagerService() {
        return batteryManagerService;
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    /**
     * Monitoring a configuration
     *
     * @return confirmation that the run request has been met (or not)
     */
    public BatteryManagementResultDTO computeNewInstructions(ZonedDateTime currentTime,
                                                             Long clientId,
                                                             EnergyProvider energyProvider,
                                                             FluxTopology fluxTopology,
                                                             FluxNode battery,
                                                             Double SoC0) {

        BatteryManagementRun newRun;

        // Checking all incomplete runs of BatteryManager for completion in between this last call to getInstructions and now
        // Then updating run states and instructions as needed
        BatteryManager batteryManager = fluxTopology.getBatteryManager();
        BatteryManagementResult batteryManagementResult;
        String interpreter = applicationProperties.getBatteryManagementProperties().getInterpreter();
        String pathToResources = applicationProperties.getBatteryManagementProperties().getPathToResources();
        if (!(batteryManager == null)) {
            // Kill previous running job (if any)
            log.debug("REST request to get the last control run status and results.");
            BatteryManagementRun lastRun = batteryManagerService.getLastRunOfClient(batteryManager, clientId, battery.getId());
            if (lastRun != null) {
                if (lastRun.getStatus().equals("running")) {
                    lastRun = batteryManagerService.killRun(batteryManager, clientId, battery.getId(), lastRun);
                    if (lastRun.getStatus().equals("running")) {
                        batteryManagementResult = new BatteryManagementResult(null, "terminated", "Failed at killing job. Aborting process.");
                        return batteryManagementResultMapper.toDto(batteryManagementResult);
                    }
                } else if (lastRun.getStatus().equals("not started")) {
                    batteryManagerService.deleteRun(batteryManager, clientId, battery.getId(), lastRun.getId());
                }
            }
        } else {
            batteryManager = new BatteryManager();
            batteryManager.setFluxTopology(fluxTopology);
            batteryManager = batteryManagerService.save(batteryManager);
            fluxTopology.setBatteryManager(batteryManager);
            log.debug("Saving batteryManager {} to the DB", batteryManager.getId());
            fluxTopologyService.update(fluxTopology);
        }
        // Create new source and new run instances
        BatteryManagementSource batteryManagementSource = new BatteryManagementSource(currentTime, energyProvider, fluxTopology, battery, SoC0);

        // Converting batteryManagementSource to JSON to provide battery management algorithm with then run it
        String suffix = Long.toString(clientId) + "_" + Long.toString(battery.getId()) + "_";
        log.debug("Create input JSON file and run Battery Management.");
        BatteryManagementSourceDTO batteryManagementSourceDTO = batteryManagementSourceMapper.toDto(batteryManagementSource);
        batteryManagementSourceDTO.toJson(pathToResources + suffix + "battery_management_input.json");

        File f = new File(pathToResources + suffix + "battery_management_input.json");
        if (f.exists() && !f.isDirectory()) {
            log.debug("Run Battery Management Script");
            BatteryManagementRun result = batteryManagerService.startNewRun(pathToResources, batteryManager, clientId, battery.getId(), suffix);

            // Convert batteryManagementResult to DTO
            BatteryManagementResultDTO batteryManagementResultDTO = batteryManagementResultMapper.toDto(result);
            return batteryManagementResultDTO;

        } else {
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Input JSON file was not created. Process aborted.");
            BatteryManagementResultDTO batteryManagementResultDTO = batteryManagementResultMapper.toDto(batteryManagementResult);
            return batteryManagementResultDTO;
        }
    }


    public BatteryManagementResultDTO computeNewInstructionsWithJson(ZonedDateTime currentTime,
                                                                     Long clientId,
                                                                     FluxTopology fluxTopology,
                                                                     Long batteryId,
                                                                     BatteryManagementSourceDTO batteryManagementSourceDTO) {

        BatteryManagementRun newRun;
        String interpreter = applicationProperties.getBatteryManagementProperties().getInterpreter();
        String pathToResources = applicationProperties.getBatteryManagementProperties().getPathToResources();

        log.debug("REST request to get the last control run status and results.");
        BatteryManagementSource batteryManagementSource;
        BatteryManagementResult batteryManagementResult;
        BatteryManager batteryManager = fluxTopology.getBatteryManager();
        if (!(batteryManager == null)) {
            // Kill previous running job (if any)
            log.debug("REST request to get the last control run status and results.");
            BatteryManagementRun lastRun = batteryManagerService.getLastRunOfClient(batteryManager, clientId, batteryId);
            if (lastRun != null) {
                if (lastRun.getStatus().equals("running")) {
                    lastRun = batteryManagerService.killRun(batteryManager, clientId, batteryId, lastRun);
                    if (lastRun.getStatus().equals("running")) {
                        batteryManagementResult = new BatteryManagementResult(null, "terminated", "Failed at killing job. Aborting process.");
                        return batteryManagementResultMapper.toDto(batteryManagementResult);
                    }
                } else if (lastRun.getStatus().equals("not started")) {
                    batteryManagerService.deleteRun(batteryManager, clientId, batteryId, lastRun.getId());
                }
            }
        } else {
            batteryManager = new BatteryManager();
            batteryManager.setFluxTopology(fluxTopology);
            batteryManager = batteryManagerService.save(batteryManager);
            fluxTopology.setBatteryManager(batteryManager);
            log.debug("Saving batteryManager {} to the DB", batteryManager.getId());
            fluxTopologyService.update(fluxTopology);
        }


        try {   // Checking format of DTO object
            batteryManagementSource = batteryManagementSourceMapper.toEntity(batteryManagementSourceDTO);
        } catch (Exception e) {
            log.info("Request body invalid formatting.");
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Request body invalid formatting.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }

        // Converting batteryManagementSource to JSON to provide battery management algorithm with then run it
        String suffix = Long.toString(clientId) + "_" + Long.toString(batteryId) + "_";
        log.debug("Create input JSON file and run Battery Management.");
        batteryManagementSource.toJson(suffix + "battery_management_input.json");


        File f = new File(suffix + "battery_management_input.json");
        if (f.exists() && !f.isDirectory()) {
            log.debug("Run Battery Management Script");
            BatteryManagementRun result = batteryManagerService.startNewRun(pathToResources, batteryManager, clientId, batteryId, suffix);
            batteryManagementResult = new BatteryManagementResult(result.getId(), result.getStatus(), result.getError(), result.getStart(), result.getEnd());
        } else {
            batteryManagementResult = new BatteryManagementResult(null, "terminated", "Input JSON file has to be provided before running the computation.");
            BatteryManagementResultDTO batteryManagementResultDTO = batteryManagementResultMapper.toDto(batteryManagementResult);
            return batteryManagementResultDTO;
        }

        // Convert batteryManagementResult into DTP
        BatteryManagementResultDTO batteryManagementResultDTO = batteryManagementResultMapper.toDto(batteryManagementResult);

        return batteryManagementResultDTO;
    }


    /**
     * Monitoring a configuration with a Json entry
     *
     * @return the list of balance data
     */
    public BatteryManagementResultDTO getInstructions(Long clientId,
                                                      FluxTopology fluxTopology,
                                                      Long batteryId,
                                                      Long runId) {

        BatteryManager batteryManager = fluxTopology.getBatteryManager();
        BatteryManagementResult batteryManagementResult;
        BatteryManagementResultDTO batteryManagementResultDTO;
        String interpreter = applicationProperties.getBatteryManagementProperties().getInterpreter();
        String pathToResources = applicationProperties.getBatteryManagementProperties().getPathToResources();

        // Checking all incomplete runs of BatteryManager for completion in between this last call to getInstructions and now
        // Then updating run states and instructions as needed
//        batteryManager = batteryManagerService.updateBatteryManagerState(batteryManager, interpreter, pathToResources);

        // Checking whether the battery manager of this topology owns this run
        if (batteryManager.getJobs().isEmpty()) {
            batteryManagementResult = new BatteryManagementResult(runId, "not authorized",
                "Run does not exist in this topology.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }
        BatteryManagementRun runToCheck = batteryManagerService.getRunById(batteryManager, clientId, batteryId, runId);
        if (runToCheck != null) {
            return batteryManagementResultMapper.toDto(runToCheck);
        } else {
            // No correspondance was found, return Error.
            log.info("Run was not found for topology {}, client {} and battery {} combination.",
                fluxTopology.getId(),
                clientId,
                batteryId);
            batteryManagementResult = new BatteryManagementResult(runId, "not authorized",
                "Run is not associated to either this client or this battery.");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }
    }

    /**
     * Monitoring a configuration with a Json entry
     *
     * @return the list of balance data
     */
    public BatteryManagementResultDTO getInstructions(Long clientId,
                                                      FluxTopology fluxTopology,
                                                      Long batteryId) {

        BatteryManager batteryManager = fluxTopology.getBatteryManager();
        BatteryManagementResult batteryManagementResult;
        BatteryManagementResultDTO batteryManagementResultDTO;
        String interpreter = applicationProperties.getBatteryManagementProperties().getInterpreter();
        String pathToResources = applicationProperties.getBatteryManagementProperties().getPathToResources();

        // Checking all incomplete runs of BatteryManager for completion in between this last call to getInstructions and now
        // Then updating run states and instructions as needed
        //batteryManager = batteryManagerService.updateBatteryManagerState(batteryManager, interpreter, pathToResources);

        // Checking whether the battery manager of this topology for the last run completed
        BatteryManagementRun lastCompletedRun = batteryManagerService.getLastCompletedRunOfClient(batteryManager, clientId, batteryId);
        if (lastCompletedRun != null) {
            return batteryManagementResultMapper.toDto(lastCompletedRun);
        } else {
            // No correspondence was found, return Error.
            log.info("Run was not found for topology {}, client {} and battery {} combination.",
                fluxTopology.getId(),
                clientId,
                batteryId);
            batteryManagementResult = new BatteryManagementResult(null, "not authorized",
                "There is no completed run for this combination (client, topology and battery device).");
            return batteryManagementResultMapper.toDto(batteryManagementResult);
        }
    }
}

