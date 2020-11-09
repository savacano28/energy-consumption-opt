package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.BatteryManagementRun;
import fr.ifpen.synergreen.domain.BatteryManager;
import fr.ifpen.synergreen.domain.FluxTopology;
import fr.ifpen.synergreen.repository.BatteryManagementRunRepository;
import fr.ifpen.synergreen.repository.BatteryManagerRepository;
import fr.ifpen.synergreen.repository.search.BatteryManagementRunSearchRepository;
import fr.ifpen.synergreen.repository.search.BatteryManagerSearchRepository;
import fr.ifpen.synergreen.service.mapper.BatteryManagementInstructionMapper;
import fr.ifpen.synergreen.service.util.WinRunScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.getCurrentTime;

/**
 * Service Implementation for managing BatteryManagementRun.
 */
@Service
@Transactional
public class BatteryManagerService {

    private final Logger log = LoggerFactory.getLogger(BatteryManagerService.class);

    private BatteryManagerRepository batteryManagerRepository;
    private BatteryManagerSearchRepository batteryManagerSearchRepository;
    private BatteryManagementRunService batteryManagementRunService;
    private BatteryManagementRunRepository batteryManagementRunRepository;
    private BatteryManagementRunSearchRepository batteryManagementRunSearchRepository;
    private BatteryManagementInstructionMapper batteryManagementInstructionMapper;
    private FluxTopologyService fluxTopologyService;
    private WinRunScriptService winRunScriptService;

    public BatteryManagerService(BatteryManagerRepository batteryManagerRepository,
                                 BatteryManagerSearchRepository batteryManagerSearchRepository,
                                 BatteryManagementRunService batteryManagementRunService,
                                 BatteryManagementRunRepository batteryManagementRunRepository,
                                 BatteryManagementRunSearchRepository batteryManagementRunSearchRepository,
                                 BatteryManagementInstructionMapper batteryManagementInstructionMapper,
                                 FluxTopologyService fluxTopologyService,
                                 WinRunScriptService winRunScriptService) {
        this.batteryManagerRepository = batteryManagerRepository;
        this.batteryManagerSearchRepository = batteryManagerSearchRepository;
        this.batteryManagementRunService = batteryManagementRunService;
        this.batteryManagementRunRepository = batteryManagementRunRepository;
        this.batteryManagementRunSearchRepository = batteryManagementRunSearchRepository;
        this.batteryManagementInstructionMapper = batteryManagementInstructionMapper;
        this.fluxTopologyService = fluxTopologyService;
        this.winRunScriptService = winRunScriptService;
    }


    /**
     * Save a BatteryManager.
     *
     * @param batteryManager the entity to save
     * @return the persisted entity
     */
    public BatteryManager save(BatteryManager batteryManager) {
        log.debug("Request to save BatteryManager : {}", batteryManager);
        BatteryManager result = batteryManagerRepository.save(batteryManager);
        batteryManagerSearchRepository.save(result);
        return result;
    }


    /**
     * Update a BatteryManager.
     *
     * @param batteryManager the entity to save
     * @return the persisted entity
     */
    public BatteryManager update(BatteryManager batteryManager) {
        log.debug("Request to update BatteryManager : {}", batteryManager);
        BatteryManager result = batteryManagerRepository.save(batteryManager);
        return result;
    }


    /**
     * Update a BatteryManager.
     *
     * @param batteryManager the entity to save
     * @return the persisted entity
     */
    public BatteryManager addToListOfJobs(BatteryManager batteryManager, BatteryManagementRun newRun) {
        log.debug("Request to update BatteryManager : {}", batteryManager);
        List<BatteryManagementRun> newListOfJobs = batteryManager.getJobs();
        newListOfJobs.add(newRun);
        batteryManager.setJobs(newListOfJobs);
        BatteryManager result = batteryManagerRepository.save(batteryManager);
        return result;
    }


    /**
     * Get one batteryManager by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BatteryManager findOne(Long id) {
        log.debug("Request to get EnergyProvider : {}", id);
        return batteryManagerRepository.findOne(id);
    }


    /**
     * Get all the BatteryManagementRuns corresponding to an existing batteryManager entity and updates their states and contents as needed
     *
     * @return the batteryManager entity as needed
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> getAllRuns(BatteryManager batteryManager) {
        log.debug("Request to get all BatteryManagementRuns of Battery Manager {}", batteryManager.getId());
        return batteryManager.getJobs();
    }


    /**
     * Get all runs for a given batteryManager, clientId and batteryId combination.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> getAllRunsOfClient(BatteryManager batteryManager, Long clientId, Long batteryId) {
        log.debug("Request to get all the runs corresponding to manager {}, client {} and battery {}",
            batteryManager.getId(), clientId, batteryId);
        List<BatteryManagementRun> result = new ArrayList<>();
        for (BatteryManagementRun run : getAllRuns(batteryManager)) {
            if (run.getClientId().equals(clientId) && run.getBatteryId().equals(batteryId)) {
                result.add(run);
            }
        }
        return result;
    }


    public BatteryManagementRun getRunById(BatteryManager batteryManager, Long clientId, Long batteryId, Long runId) {
        List<BatteryManagementRun> listOfJObs = getAllRunsOfClient(batteryManager, clientId, batteryId);
        if (!listOfJObs.isEmpty()) {
            for (BatteryManagementRun run : listOfJObs) {
                if (run.getId().equals(runId)) {
                    return run;
                }
            }
            return null;
        } else {
            return null;
        }

    }

    /**
     * Get all COMPLETED runs for a given batteryManager, clientId and batteryId combination.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> getAllCompletedRunsOfClient(BatteryManager batteryManager, Long clientId, Long batteryId) {
        log.debug("Request to get all the runs corresponding to manager {}, client {} and battery {}",
            batteryManager.getId(), clientId, batteryId);
        List<BatteryManagementRun> result = new ArrayList<>();
        for (BatteryManagementRun run : getAllRuns(batteryManager)) {
            if (run.getClientId().equals(clientId) && run.getBatteryId().equals(batteryId) && run.getStatus().toLowerCase().equals("completed")) {
                result.add(run);
            }
        }
        return result;
    }


    /**
     * Get last run for a given batteryManager, clientId and batteryId combination.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public BatteryManagementRun getLastRunOfClient(BatteryManager batteryManager, Long clientId, Long batteryId) {
        log.debug("Request to get the last run for manager {}, client {} and battery {}",
            batteryManager.getId(), clientId, batteryId);
        List<BatteryManagementRun> listOfJobs = getAllRunsOfClient(batteryManager, clientId, batteryId);
        if (!listOfJobs.isEmpty()) {
            BatteryManagementRun result = listOfJobs.get(0);
            ZonedDateTime closestDatetime = result.getStart();
            listOfJobs.remove(0);
            for (BatteryManagementRun run : listOfJobs) {
                if (run.getStart().toInstant().compareTo(closestDatetime.toInstant()) < 0) {
                    result = run;
                    closestDatetime = result.getStart();
                }
            }
            log.debug("Last run for manager {}, client {} and battery {} : runId {}.",
                batteryManager.getId(), clientId, batteryId, result.getId());
            return result;
        } else {
            log.debug("No existing run referenced for manager {}, client {} and battery {}.",
                batteryManager.getId(), clientId, batteryId);
            return null;
        }
    }


    /**
     * Get last COMPLETED run for a given batteryManager, clientId and batteryId combination.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public BatteryManagementRun getLastCompletedRunOfClient(BatteryManager batteryManager, Long clientId, Long batteryId) {
        log.debug("Request to get the last completed run for manager, client {} and battery {}", clientId, batteryId);

        if (batteryManager != null) {
            List<BatteryManagementRun> listOfJobs = getAllCompletedRunsOfClient(batteryManager, clientId, batteryId);
            if (!listOfJobs.isEmpty()) {
                BatteryManagementRun result = listOfJobs.get(listOfJobs.size() - 1);
                log.debug("Last completed run for manager {}, client {} and battery {} : runId {}.",
                    batteryManager.getId(), clientId, batteryId, result.getId());
                return result;
            } else {
                log.debug("No previous completed run existing for manager {}, client {} and battery {}.",
                    batteryManager.getId(), clientId, batteryId);
                return null;
            }
        } else {
            log.debug("No previous completed run existing for manager, client and battery combination.");
            return null;
        }
    }


    /**
     * Start new Run for a given batteryManager, clientId and batteryId combination.
     *
     * @return the confirmation that it's done (or not)
     */
    @Transactional(readOnly = true)
    public BatteryManagementRun startNewRun(String pathToResources,
                                            BatteryManager batteryManager, Long clientId,
                                            Long batteryId, String suffix) {
        log.debug("Request to launch a new run for manager {}, client {} and battery {}",
            batteryManager.getId(), clientId, batteryId);

        // Creating a new BatteryManagementRun instance
        BatteryManagementRun newRun = new BatteryManagementRun(clientId, batteryId, "not started");
        newRun.setBatteryManager(batteryManager);
        log.debug("New runId : {}.", newRun.getId());
        newRun = batteryManagementRunService.save(newRun);

        File f1 = new File(pathToResources + suffix + "battery_management_input.json");
        suffix = Long.toString(newRun.getId()) + "_" + suffix;
        String inFilename = pathToResources + suffix + "battery_management_input.json";
        String outFilename = inFilename.replace("input", "output");
        File f2 = new File(inFilename);
        try {
            boolean result = Files.deleteIfExists(f2.toPath());     // Delete previous file if existing
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (f1.renameTo(f2)) {
            log.info("Renaming input json file to incorporate newRun's id {}.", newRun.getId());
            log.info("Saving new run {} into DB", newRun.getId());
            batteryManager.getJobs().add(newRun);
            batteryManager = save(batteryManager);
            log.info("Added run {} to batteryManager {} list of jobs", newRun.getId(), batteryManager.getId());
            return winRunScriptService.execBatteryManagement(newRun, inFilename, outFilename);
        } else {
            log.error("Renaming input json file failed.");
            ZonedDateTime endTime = getCurrentTime().truncatedTo(ChronoUnit.SECONDS);
            return batteryManagementRunService.updateRun(newRun, "terminated", "Incident during renaming process. " +
                "Input json file may not exist. Process aborted.", endTime);
        }
    }


    /**
     * Kill run still running.
     *
     * @return confirmation that it's done (or not)
     */
    @Transactional(readOnly = true)
    public BatteryManagementRun killRun(BatteryManager batteryManager, Long clientId, Long batteryId, BatteryManagementRun runToKill) {
        List<String> processResult = winRunScriptService.killRunningProcess(runToKill.getProcessId());
        if (processResult.get(0).contains("le processus avec PID " + runToKill.getProcessId() + " a été terminé.")) {
//            deleteRun(batteryManager, clientId, batteryId, runToKill.getId());    // Commented for now to leave a trace of the aborted run in the DB
            runToKill = batteryManagementRunService.updateRun(runToKill, "terminated", "A new run of equal entries was requested while this one was still running.",
                getCurrentTime().truncatedTo(ChronoUnit.SECONDS));
        } else {
            log.error("Failed attempt at killing previous running job.");
        }
        return runToKill;
    }


    /**
     * Delete run from Battery Manager job list for a given batteryManager, clientId and batteryId combination.
     */
    @Transactional(readOnly = true)
    public void deleteRun(BatteryManager batteryManager, Long clientId, Long batteryId, Long runId) {
        log.debug("Request to delete run {} from battery manager job list", runId);
        List<BatteryManagementRun> listOfJobs = getAllRunsOfClient(batteryManager, clientId, batteryId);
        BatteryManagementRun runToRemove = batteryManagementRunService.findOneSimple(clientId, runId);
        if (runToRemove != null) {
            listOfJobs.remove(runToRemove);
            batteryManager.setJobs(listOfJobs);
            batteryManagementRunService.delete(runId);
            log.debug("Previous job deleted");
        }
        batteryManagerRepository.save(batteryManager);
    }


    /**
     * Get all the BatteryManagementRuns for combination (clientId, batteryId).
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> getAllRuns(Long clientId, Long batteryId) {
        log.debug("Request to get all BatteryManagementRuns");
        List<BatteryManagementRun> result = new ArrayList<>();
        for (BatteryManagementRun run : batteryManagementRunRepository.findAll()) {
            if (run.getClientId().equals(clientId) && run.getBatteryId().equals(batteryId)) {
                result.add(run);
            }
        }
        return result;
    }

    /**
     * Get run of id {runId} if the combination associated to it is clientId. Otherwise, returning null
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public BatteryManagementRun findOneSimple(Long clientId, Long runId) {
        log.debug("Request to get all BatteryManagementRuns");
        BatteryManagementRun result = batteryManagementRunRepository.findOne(runId);
        if (result == null) {
            log.info("Run of id {} was not found.", runId);
            return null;
        } else {
            if (result.getClientId().equals(clientId)) {
                return result;
            } else {
                log.info("Run {id} is not associated to given client, topology and battery combination. Access not authorized.", runId);
                return null;
            }
        }
    }


    /**
     * Monitoring a configuration with a Json entry
     *
     * @return the list of balance data
     */
    public BatteryManager updateBatteryManagerState(BatteryManager batteryManager, String interpreter, String pathToResources) {

        if (!batteryManager.getJobs().equals(null)) {
            for (BatteryManagementRun run : batteryManager.getJobs()) {
                if (!run.getStatus().equals("completed") && !run.getStatus().equals("terminated")) {
                    // Check if run is still running
                    List<Long> ongoingTasklist = winRunScriptService.getRunningProcesses(interpreter);
                    if (!ongoingTasklist.isEmpty()) {
                        if (!ongoingTasklist.contains(run.getProcessId())) {
                            // Process finished in the meanwhile.
                            // Checking whether it's because it's completed of if it's been terminated for whatever reason.
                            String suffix = Long.toString(run.getId()) + "_"
                                + Long.toString(run.getClientId()) + "_"
                                + Long.toString(run.getBatteryId()) + "_";
                            String inFilename = pathToResources + suffix + "battery_management_input.json";
                            String outFilename = inFilename.replace("input", "output");
                            run = batteryManagementRunService.saveFinalRunState(run, inFilename, outFilename);
                            FluxTopology fluxTopology = run.getBatteryManager().getFluxTopology();
                            fluxTopologyService.update(fluxTopology);
                        }
                    }
                }
            }
        }
        return batteryManager;
    }

    /**
     * Delete the BatteryManagementRun by id.
     *
     * @param runId the id of the BatteryManagementRun entity
     */
    public void delete(Long runId) {
        log.debug("Request to delete BatteryManagementRun : {}", runId);
        batteryManagementRunRepository.delete(runId);
        batteryManagementRunSearchRepository.delete(runId);
    }


}
