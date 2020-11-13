package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ifpen.synergreen.domain.*;
import fr.ifpen.synergreen.domain.enumeration.DataType;
import fr.ifpen.synergreen.service.BatteryManagementInstructionService;
import fr.ifpen.synergreen.service.BatteryManagementRunService;
import fr.ifpen.synergreen.service.BatteryManagementService;
import fr.ifpen.synergreen.service.EnergyManagementSystemService;
import fr.ifpen.synergreen.service.dto.BatteryManagementResultDTO;
import fr.ifpen.synergreen.service.dto.BatteryManagementSourceDTO;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import fr.ifpen.synergreen.service.mapper.BatteryManagementInstructionMapper;
import fr.ifpen.synergreen.service.mapper.BatteryManagementResultMapper;
import fr.ifpen.synergreen.web.rest.errors.BadRequestAlertException;
import fr.ifpen.synergreen.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.epochToZonedDateTime;
import static fr.ifpen.synergreen.service.util.StringFormatUtils.instructionDateToInstant;

/**
 * REST controller for managing EnergyManagementSystem.
 */
@RestController
@RequestMapping("/api")
public class EnergyManagementSystemResource {

    private final Logger log = LoggerFactory.getLogger(EnergyManagementSystemResource.class);

    private static final String ENTITY_NAME = "energyManagementSystem";

    private final EnergyManagementSystemService energyManagementSystemService;


    public EnergyManagementSystemResource(EnergyManagementSystemService energyManagementSystemService) {
        this.energyManagementSystemService = energyManagementSystemService;
    }


    /**
     * POST  /energy-management-systems : Create a new energyManagementSystem.
     *
     * @param energyManagementSystem the energyManagementSystem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new energyManagementSystem, or with status 400 (Bad Request) if the energyManagementSystem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/energy-management-systems")
    @Timed
    public ResponseEntity<EnergyManagementSystem> createEnergyManagementSystem(@RequestBody EnergyManagementSystem energyManagementSystem) throws URISyntaxException {
        log.debug("REST request to save EnergyManagementSystem : {}", energyManagementSystem);
        if (energyManagementSystem.getId() != null) {
            throw new BadRequestAlertException("A new energyManagementSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnergyManagementSystem result = energyManagementSystemService.save(energyManagementSystem);
        return ResponseEntity.created(new URI("/api/energy-management-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /energy-management-systems : Updates an existing energyManagementSystem.
     *
     * @param energyManagementSystem the energyManagementSystem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated energyManagementSystem,
     * or with status 400 (Bad Request) if the energyManagementSystem is not valid,
     * or with status 500 (Internal Server Error) if the energyManagementSystem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/energy-management-systems")
    @Timed
    public ResponseEntity<EnergyManagementSystem> updateEnergyManagementSystem(@RequestBody EnergyManagementSystem energyManagementSystem) throws URISyntaxException {
        log.debug("REST request to update EnergyManagementSystem : {}", energyManagementSystem);
        if (energyManagementSystem.getId() == null) {
            return createEnergyManagementSystem(energyManagementSystem);
        }
        EnergyManagementSystem result = energyManagementSystemService.save(energyManagementSystem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, energyManagementSystem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /energy-management-systems : get all the energyManagementSystems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of energyManagementSystems in body
     */
    @GetMapping("/energy-management-systems")
    @Timed
    public List<EnergyManagementSystem> getAllEnergyManagementSystems() {
        log.debug("REST request to get all EnergyManagementSystems");
        return energyManagementSystemService.findAll();
    }

    /**
     * GET  /energy-management-systems/:id : get the "id" energyManagementSystem.
     *
     * @param id the id of the energyManagementSystem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the energyManagementSystem, or with status 404 (Not Found)
     */
    @GetMapping("/energy-management-systems/{id}")
    @Timed
    public ResponseEntity<EnergyManagementSystem> getEnergyManagementSystem(@PathVariable Long id) {
        log.debug("REST request to get EnergyManagementSystem : {}", id);
        EnergyManagementSystem energyManagementSystem = energyManagementSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(energyManagementSystem));
    }

    /**
     * DELETE  /energy-management-systems/:id : delete the "id" energyManagementSystem.
     *
     * @param id the id of the energyManagementSystem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/energy-management-systems/{id}")
    @Timed
    public ResponseEntity<Void> deleteEnergyManagementSystem(@PathVariable Long id) {
        log.debug("REST request to delete EnergyManagementSystem : {}", id);
        energyManagementSystemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/energy-management-systems?query=:query : search for the energyManagementSystem corresponding
     * to the query.
     *
     * @param query the query of the energyManagementSystem search
     * @return the result of the search
     */
    @GetMapping("/_search/energy-management-systems")
    @Timed
    public List<EnergyManagementSystem> searchEnergyManagementSystems(@RequestParam String query) {
        log.debug("REST request to search EnergyManagementSystems for query {}", query);
        return energyManagementSystemService.search(query);
    }

    /**
     * Monitoring state of system
     *
     * @return the result of the monitoring every step
     */
    @GetMapping(value = "/energy-management-systems/monitoring")
    @Timed
    public List<StateFluxNodeDTO> monitoring(@RequestParam Long id,
                                             @RequestParam ZonedDateTime start,
                                             @RequestParam ZonedDateTime end,
                                             @RequestParam Long step) { //@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")


        LocalDateTime s = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
        start = s.atZone(ZoneId.systemDefault());
        s = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
        end = s.atZone(ZoneId.systemDefault());

        log.debug("REST request to monitoring for query {}", id);
        return updateGraphicData(energyManagementSystemService.monitoring(id, start, end, step), start, end);
    }

    /*moyen points , make a classe utils for this ! !! */
    private List<StateFluxNodeDTO> updateGraphicData(List<StateFluxNodeDTO> list, ZonedDateTime start, ZonedDateTime end) {
        int diffD = end.getDayOfYear() - start.getDayOfYear();
        int diffM = end.getMonthValue() - start.getMonthValue();

        list.stream()
            .forEach(c -> {
                if (diffD > 365) {
                    c.setpConsoGlobal(makeAProcessedData(c.getpConsoGlobal(), "month"));
                    c.setpProdGlobal(makeAProcessedData(c.getpProdGlobal(), "month"));
                    c.setpBatGlobal(makeAProcessedData(c.getpBatGlobal(), "month"));
                    c.setpConsoFromProd(makeAProcessedData(c.getpConsoFromProd(), "month"));
                    c.setpConsoFromBat(makeAProcessedData(c.getpConsoFromBat(), "month"));
                    c.setpConsoFromGrid(makeAProcessedData(c.getpConsoFromGrid(), "month"));
                    c.setpProdConsByConsumers(makeAProcessedData(c.getpProdConsByConsumers(), "month"));
                    c.setpProdConsByBat(makeAProcessedData(c.getpProdConsByBat(), "month"));
                    c.setpProdSentToGrid(makeAProcessedData(c.getpProdSentToGrid(), "month"));
                } else if (diffM > 0 && diffD < 365) {
                    c.setpConsoGlobal(makeAProcessedData(c.getpConsoGlobal(), "week"));
                    c.setpProdGlobal(makeAProcessedData(c.getpProdGlobal(), "week"));
                    c.setpBatGlobal(makeAProcessedData(c.getpBatGlobal(), "week"));
                    c.setpConsoFromProd(makeAProcessedData(c.getpConsoFromProd(), "week"));
                    c.setpConsoFromBat(makeAProcessedData(c.getpConsoFromBat(), "week"));
                    c.setpConsoFromGrid(makeAProcessedData(c.getpConsoFromGrid(), "week"));
                    c.setpProdConsByConsumers(makeAProcessedData(c.getpProdConsByConsumers(), "week"));
                    c.setpProdConsByBat(makeAProcessedData(c.getpProdConsByBat(), "week"));
                    c.setpProdSentToGrid(makeAProcessedData(c.getpProdSentToGrid(), "week"));
                } else if (diffD > 0 && diffM < 1) {
                    c.setpConsoGlobal(makeAProcessedData(c.getpConsoGlobal(), "hour"));
                    c.setpProdGlobal(makeAProcessedData(c.getpProdGlobal(), "hour"));
                    c.setpBatGlobal(makeAProcessedData(c.getpBatGlobal(), "hour"));
                    c.setpConsoFromProd(makeAProcessedData(c.getpConsoFromProd(), "hour"));
                    c.setpConsoFromBat(makeAProcessedData(c.getpConsoFromBat(), "hour"));
                    c.setpConsoFromGrid(makeAProcessedData(c.getpConsoFromGrid(), "hour"));
                    c.setpProdConsByConsumers(makeAProcessedData(c.getpProdConsByConsumers(), "hour"));
                    c.setpProdConsByBat(makeAProcessedData(c.getpProdConsByBat(), "hour"));
                    c.setpProdSentToGrid(makeAProcessedData(c.getpProdSentToGrid(), "hour"));
                }
            });

        return list; //rapport considerar les groupping

    }

    private List<MeasuredData> makeAProcessedData(List<MeasuredData> list, String s) {
        Map<ZonedDateTime, Double> processedData = new LinkedHashMap<>();
        Map<ZonedDateTime, Double> addMeasures = new LinkedHashMap<>();
        Map<ZonedDateTime, Integer> countSteps = new LinkedHashMap<>();

        list.stream().forEach(m -> {
            ZonedDateTime step = m.getInstant();
            switch (s) {
                case ("month"):
                    ZonedDateTime month = step.with(LocalDateTime.of(step.getYear(), step.getMonth(), YearMonth.of(step.getYear(), step.getMonth()).lengthOfMonth(), 0, 0, 0, 0));
                    if (processedData.containsKey(month)) {
                        addMeasures.put(month, addMeasures.get(month) + m.getMeasure());
                        countSteps.put(month, countSteps.get(month) + 1);
                        processedData.put(month, addMeasures.get(month)/countSteps.get(month));
                    } else {
                        processedData.put(month, m.getMeasure());
                        addMeasures.put(month, m.getMeasure());
                        countSteps.put(month, 1);
                    }
                    break;
                case ("week"):
                    Integer lastDay = YearMonth.of(step.getYear(), step.getMonth()).lengthOfMonth();
                    ZonedDateTime week = step.with(LocalDateTime.of(
                        step.getYear(),
                        step.getMonth(),
                        /*(step.getDayOfWeek().equals(DayOfWeek.MONDAY) ? (Math.min(step.getDayOfMonth() + 6, lastDay)) : //comparer with the last hour of period evaluation
                            (step.getDayOfWeek().equals(DayOfWeek.TUESDAY) ? (Math.min(step.getDayOfMonth() + 5, lastDay)) :
                                (step.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) ? (Math.min(step.getDayOfMonth() + 4, lastDay)) :
                                    (step.getDayOfWeek().equals(DayOfWeek.THURSDAY) ? (Math.min(step.getDayOfMonth() + 3, lastDay)) :
                                        (step.getDayOfWeek().equals(DayOfWeek.FRIDAY) ? (Math.min(step.getDayOfMonth() + 2, lastDay)) :
                                            (step.getDayOfWeek().equals(DayOfWeek.SATURDAY) ? (Math.min(step.getDayOfMonth() + 1, lastDay)) : step.getDayOfMonth())))))),*/

                        (step.getDayOfWeek().equals(DayOfWeek.SUNDAY) ? (Math.max(step.getDayOfMonth() - 6, 1)) : //comparer with the last hour of period evaluation
                            (step.getDayOfWeek().equals(DayOfWeek.SATURDAY) ? (Math.max(step.getDayOfMonth() - 5, 1)) :
                                (step.getDayOfWeek().equals(DayOfWeek.FRIDAY) ? (Math.max(step.getDayOfMonth() - 4, 1)) :
                                    (step.getDayOfWeek().equals(DayOfWeek.THURSDAY) ? (Math.max(step.getDayOfMonth()- 3, 1)) :
                                        (step.getDayOfWeek().equals(DayOfWeek.WEDNESDAY) ? (Math.max(step.getDayOfMonth() - 2, 1)) :
                                            (step.getDayOfWeek().equals(DayOfWeek.TUESDAY) ? (Math.max(step.getDayOfMonth() - 1, 1)) : step.getDayOfMonth())))))),
                        0,
                        0,
                        0,
                        0));
                    if (processedData.containsKey(week)) {
                        addMeasures.put(week, addMeasures.get(week) + m.getMeasure());
                        countSteps.put(week, countSteps.get(week) + 1);
                        processedData.put(week, addMeasures.get(week) /countSteps.get(week));
                    } else {
                        processedData.put(week, m.getMeasure());
                        addMeasures.put(week, m.getMeasure());
                        countSteps.put(week, 1);
                    }
                    break;
                case ("hour"):
                    ZonedDateTime hour = step.with(LocalDateTime.of(
                        step.getYear(),
                        step.getMonth(),
                        step.getDayOfMonth(),
                        step.getHour(), 0, 0, 0));
                    if (processedData.containsKey(hour)) {
                        addMeasures.put(hour, addMeasures.get(hour) + m.getMeasure());
                        countSteps.put(hour, countSteps.get(hour) + 1);
                        processedData.put(hour, addMeasures.get(hour) / countSteps.get(hour));
                    } else {
                        processedData.put(hour, m.getMeasure());
                        addMeasures.put(hour, m.getMeasure());
                        countSteps.put(hour, 1);
                    }
                    break;
            }
        });

        if(!list.isEmpty()){
            processedData.put(list.get(list.size()-1).getInstant(),list.get(list.size()-1).getMeasure());}

        return processedData.entrySet()
            .stream()
            .map(c -> new MeasuredData(
                c.getKey(),
                c.getValue(),
                true,
                DataType.POWER))
            .collect(Collectors.toList());

    }

    /**
     * Controlling a Storage Node -- Real DB properties
     *
     * @return the id for the new control run
     */
    @GetMapping("/energy-management-systems/battery-management/computeNewInstructions")
    @Timed
    public ResponseEntity<BatteryManagementResultDTO> computeNewInstructions(@RequestParam Long clientId,
                                                                             @RequestParam Long fluxTopologyId,
                                                                             @RequestParam Long batteryId,
                                                                             @RequestParam(required = false) Long time,
                                                                             @RequestParam(required = false) Double pBat0,
                                                                             @RequestParam Double SoC0) {
        log.debug("REST request to compute new instructions for battery {} in topology {}.", fluxTopologyId, batteryId);
        log.info("REST request to compute new instructions for battery {} in topology {}.", fluxTopologyId, batteryId);

        BatteryManagementResultDTO batteryManagementResultDTO;
        if (pBat0 != null) {
            ZonedDateTime measureDate = epochToZonedDateTime(time);
            batteryManagementResultDTO = energyManagementSystemService.computeNewInstructions(clientId, fluxTopologyId, batteryId, measureDate, pBat0, SoC0);
            log.info("Sending response to WS");
        } else {
            batteryManagementResultDTO = energyManagementSystemService.computeNewInstructions(clientId, fluxTopologyId, batteryId, SoC0);
            log.info("Sending response to WS");
        }


        return ResponseEntity.ok().body(batteryManagementResultDTO);
    }


    /**
     * Controlling a Storage Node -- Test mode with JSON Input
     *
     * @return the id for the new control run
     */
    @PutMapping("/energy-management-systems/battery-management/computeNewInstructionsWithJson")
    @Timed
    public ResponseEntity<BatteryManagementResultDTO> computeNewInstructionsWithJson(@RequestParam Long clientId,
                                                                                     @RequestParam Long fluxTopologyId,
                                                                                     @RequestParam Long batteryId,
                                                                                     @RequestBody BatteryManagementSourceDTO batteryManagementSourceDTO) {

        log.debug("REST request to compute new instructions for battery {} in topology {}.", fluxTopologyId, batteryId);

        BatteryManagementResultDTO batteryManagementResultDTO = energyManagementSystemService.computeNewInstructionsWithJson(clientId, fluxTopologyId, batteryId, batteryManagementSourceDTO);

        return ResponseEntity.ok().body(batteryManagementResultDTO);
    }


    /**
     * Controlling a Storage Node -- Get Result based on the run Id
     *
     * @return the status and the result of the controlling every step if available
     */
    @GetMapping("/energy-management-systems/battery-management/getInstructions")
    @Timed
    public ResponseEntity<BatteryManagementResultDTO> getInstructions(@RequestParam Long clientId,
                                                                      @RequestParam Long fluxTopologyId,
                                                                      @RequestParam Long batteryId,
                                                                      @RequestParam (required = false) Long runId) {

        log.debug("REST request to get run {} status and results.", runId);
        BatteryManagementResultDTO batteryManagementResultDTO;
        if (!(runId == null)) {
            batteryManagementResultDTO = energyManagementSystemService.getInstructions(clientId, fluxTopologyId, batteryId, runId);
        } else {
            batteryManagementResultDTO = energyManagementSystemService.getInstructions(clientId, fluxTopologyId, batteryId);
        }

        return ResponseEntity.ok().body(batteryManagementResultDTO);
    }

   /* *//**
     * Controlling a Storage Node -- Get Result based on the run Id
     *
     * @return the status and the result of the controlling every step if available
     *//*
    @GetMapping("/energy-management-systems/battery-management/getInstructionsFake")
    @Timed
    public ResponseEntity<BatteryManagementResultDTO> getInstructionsFake(@RequestParam Long clientId,
                                                                          @RequestParam Long fluxTopologyId,
                                                                          @RequestParam Long batteryId,
                                                                          @RequestParam(required = false) Long runId) {

        log.debug("REST request to get run {} status and results.", runId);
        BatteryManagementResultDTO resultDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("getInstructions_run-completed.json");
            InputStream is = resource.getInputStream();
            resultDTO = objectMapper.readValue(is, BatteryManagementResultDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(resultDTO);

    }*/


}
