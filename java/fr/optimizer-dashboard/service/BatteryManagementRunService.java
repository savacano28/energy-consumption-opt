package fr.ifpen.synergreen.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import fr.ifpen.synergreen.domain.BatteryManagementInstruction;
import fr.ifpen.synergreen.domain.BatteryManagementRun;
import fr.ifpen.synergreen.repository.BatteryManagementRunRepository;
import fr.ifpen.synergreen.repository.search.BatteryManagementRunSearchRepository;
import fr.ifpen.synergreen.service.dto.BatteryManagementInstructionDTO;
import fr.ifpen.synergreen.service.mapper.BatteryManagementInstructionMapper;
import fr.ifpen.synergreen.service.util.StringFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.epochToZonedDateTime;


/**
 * Service Implementation for managing BatteryManagementRun.
 */
@Service
@Transactional
public class BatteryManagementRunService {

    private final Logger log = LoggerFactory.getLogger(BatteryManagementRunService.class);

    private BatteryManagementRunRepository batteryManagementRunRepository;
    private BatteryManagementRunSearchRepository batteryManagementRunSearchRepository;
    private BatteryManagementInstructionService batteryManagementInstructionService;
    private BatteryManagementInstructionMapper batteryManagementInstructionMapper;


    public BatteryManagementRunService(BatteryManagementRunRepository batteryManagementRunRepository,
                                       BatteryManagementRunSearchRepository batteryManagementRunSearchRepository,
                                       BatteryManagementInstructionService batteryManagementInstructionService,
                                       BatteryManagementInstructionMapper batteryManagementInstructionMapper) {
        this.batteryManagementRunRepository = batteryManagementRunRepository;
        this.batteryManagementRunSearchRepository = batteryManagementRunSearchRepository;
        this.batteryManagementInstructionService = batteryManagementInstructionService;
        this.batteryManagementInstructionMapper = batteryManagementInstructionMapper;
    }

    /**
     * Save a BatteryManagementRun.
     *
     * @param batteryManagementRun the entity to save
     * @return the persisted entity
     */
    public BatteryManagementRun save(BatteryManagementRun batteryManagementRun) {
        log.debug("Request to save BatteryManagementRun");
        if (!batteryManagementRun.getInstructions().isEmpty()) {
            List<BatteryManagementInstruction> newListOfInstructions = new ArrayList<>();
            for (BatteryManagementInstruction instruction : batteryManagementRun.getInstructions()) {
                instruction = batteryManagementInstructionService.save(instruction);
                newListOfInstructions.add(instruction);
            }
            batteryManagementRun.setInstructions(newListOfInstructions);
        }
        BatteryManagementRun result = batteryManagementRunRepository.save(batteryManagementRun);
        batteryManagementRunSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the BatteryManagementRuns.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> findAll() {
        log.debug("Request to get all BatteryManagementRuns");
        return batteryManagementRunRepository.findAll();
    }

    /**
     * Get all the BatteryManagementRuns for combination (clientId, fluxTopologyId, batteryId).
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementRun> findAllWithDetails(Long clientId, Long batteryId) {
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
     * Get run of id {runId} if the combination associated to it is clientId, fluxTopologyId, batteryId). Otherwise, returning null
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

    @Transactional(readOnly = true)
    public BatteryManagementRun findOneSimple(Long runId) {
        log.debug("Request to get all BatteryManagementRuns");
        BatteryManagementRun result = batteryManagementRunRepository.findOne(runId);
        if (result == null) {
            log.info("Run of id {} was not found.", runId);
            return null;
        } else {
            return result;
        }
    }


    /**
     * Updates an existing BatteryManagementRun upon start
     *
     * @param run       batteryManagementRun object
     * @param processId Windows' PID
     * @param status    run status
     * @param start     date of execution start
     */
    public BatteryManagementRun updateRun(BatteryManagementRun run, String status, ZonedDateTime start, Long processId) {
        log.debug("Changing Information for Run: {}", run.getId());
        if (run != null) {
            run.setStatus(status);
            run.setProcessId(processId);
            run.setStart(start);
//            run = save(run);
        }
        return run;
    }


    /**
     * Updates an existing BatteryManagementRun
     *
     * @param run       batteryManagementRun object
     * @param processId Windows' PID
     * @param status    run status
     * @param end       date of execution stop (normal end of computing)
     */
    public BatteryManagementRun updateRun(BatteryManagementRun run, String status, ZonedDateTime end, Long processId, List<BatteryManagementInstruction> listOfInstructions) {
        log.debug("Changing Information for Run: {}", run.getId());
        if (run != null) {
            run.setProcessId(null);
            run.setStatus(status);
            run.setEnd(end);
            run.setInstructions(listOfInstructions);
//            run = save(run);
        }
        return run;
    }


    public BatteryManagementRun updateRun(BatteryManagementRun run, String status, ZonedDateTime start, ZonedDateTime end,
                                          Long processId, List<BatteryManagementInstruction> listOfInstructions) {
        log.debug("Changing Information for Run: {}", run.getId());
        if (run != null) {
            run.setProcessId(processId);
            run.setStatus(status);
            run.setStart(start);
            run.setEnd(end);
            run.setInstructions(listOfInstructions);
            run = save(run);
        }
        return run;
    }

    /**
     * Updates an existing BatteryManagementRun
     *
     * @param run    batteryManagementRun object
     * @param status run status
     * @param error  description of fatal error which took place during run
     * @param end    date of execution stop because of an error
     */
    public BatteryManagementRun updateRun(BatteryManagementRun run, String status, String error, ZonedDateTime end) {
        log.debug("Changing Information for Run: {}", run.getId());
        if (run != null) {
            run.setProcessId(null);
            run.setStatus(status);
            run.setError(error);
            run.setEnd(end);
            run = save(run);
        }
        return run;
    }


    public BatteryManagementRun saveFinalRunState(BatteryManagementRun run, String inFilename, String outFilename) {

        // If output file exists, then it's been properly finished after the computation ended. Thus, checking if output exists in resources/
        File fIn = new File(inFilename);
        File fOut = new File(outFilename);
        ZonedDateTime end;
        if (fOut.exists() && !fOut.isDirectory()) {
            // Update run and save instructions. Then, delete output file.
            end = epochToZonedDateTime(fOut.lastModified() / 1000L);
            ZonedDateTime startComputation = null;
            ZonedDateTime endComputation = null;

            //create JsonParser object
            try {
                JsonParser jsonParser = new JsonFactory().createParser(new File(outFilename));
                List<BatteryManagementInstruction> listOfInstructions = new ArrayList<>();
                BatteryManagementInstructionDTO instructionDTO;
                BatteryManagementInstruction instruction;
                //loop through the tokens
                try {
                    //loop through the JsonTokens
                    jsonParser.nextToken();
                    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                        //if (startComputation != null && endComputation != null && listOfInstructions.size() != 0) break;
                        String name = jsonParser.getCurrentName();
                        String key;
                        String value;
                        if ("result".toLowerCase().equals(name.toLowerCase())) {
                            jsonParser.nextToken();
                            while ((jsonParser.nextToken() != JsonToken.END_ARRAY)) {
                                instructionDTO = new BatteryManagementInstructionDTO();
                                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                                    jsonParser.nextToken();
                                    key = jsonParser.getCurrentName();
                                    value = jsonParser.getText();
                                    if ("time".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setDate(value);
                                    } else if ("PPV".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setpProdGlobal(Double.valueOf(value));
                                    } else if ("deltaPPV".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setDeltapProdGlobal(Double.valueOf(value));
                                    } else if ("lambdaPV".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setLambdaPV(Double.valueOf(value));
                                    } else if ("Conso".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setpConsoGlobal(Double.valueOf(value));
                                    } else if ("deltaConso".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setDeltapConsoGlobal(Double.valueOf(value));
                                    } else if ("lambdaConso".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setLambdapConsoGlobal(Double.valueOf(value));
                                    } else if ("pBat".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setP_bat(Double.valueOf(value));
                                    } else if ("soc".toLowerCase().equals(key.toLowerCase())) {
                                        instructionDTO.setSoc(Double.valueOf(value));
                                    }
                                }
                                instruction = batteryManagementInstructionMapper.toEntity(instructionDTO);
                                instruction.setBatteryManagementRun(run);
                                instruction = batteryManagementInstructionService.save(instruction);
                                listOfInstructions.add(instruction);
                            }
                        } else if ("start".toLowerCase().equals(name.toLowerCase())) {
                            jsonParser.nextToken();
                            value = jsonParser.getText();
                            startComputation = StringFormatUtils.epochToZonedDateTime(Math.round(Double.valueOf(value)));
                        } else if ("end".toLowerCase().equals(name.toLowerCase())) {
                            jsonParser.nextToken();
                            value = jsonParser.getText();
                            endComputation = StringFormatUtils.epochToZonedDateTime(Math.round(Double.valueOf(value)));
                        }
                    }
                    jsonParser.close();
                    run = updateRun(run, "completed", startComputation, endComputation, null, listOfInstructions);

                    // Spring cleaning : deleting input and output files that were just processed.
//                    if (fIn.exists() && !fIn.isDirectory()) {
//                        if (fIn.delete()) {
//                            log.info("Input File deleted successfully after processing");
//                        } else {
//                            log.error("Failed to delete the input file");
//                        }
//                    }
//                    if (fOut.delete()) {
//                        log.info("Output File deleted successfully after processing");
//                    } else {
//                        log.error("Failed to delete the output file");
//                    }
                } catch (JsonParseException e) {
                    log.error(e.getMessage());
                    run = updateRun(run, "terminated", "JsonParseException " + e.getMessage(), end);

                }

            } catch (IOException e) {
                log.error(e.getMessage());
                run = updateRun(run, "terminated", "IOException " + e.getMessage(), end);
            }
        } else {
            log.error("Output not existing: unknown error during computation.");
            run = updateRun(run, "terminated", "Unknown error during computation. " +
                "Possible cause: iteration limit exceeded or process timeout.", null);
        }
        return run;
    }


    /**
     * Delete the BatteryManagementRun by id.
     *
     * @param runId the id of the BatteryManagementRun entity
     */
    public void delete(Long runId) {
        log.debug("Request to delete BatteryManagementRun {} and the corresponding  instructions", runId);
        BatteryManagementRun run = batteryManagementRunRepository.findOne(runId);
        for (BatteryManagementInstruction instruction : run.getInstructions()) {
            batteryManagementInstructionService.delete(instruction.getId());
        }
        batteryManagementRunRepository.delete(runId);
        batteryManagementRunSearchRepository.delete(runId);
    }

}
