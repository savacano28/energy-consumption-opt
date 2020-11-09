package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.BatteryManagementInstruction;
import fr.ifpen.synergreen.repository.BatteryManagementInstructionRepository;
import fr.ifpen.synergreen.repository.search.BatteryManagementInstructionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing BatteryManagementRun.
 */
@Service
@Transactional
public class BatteryManagementInstructionService {

    private final Logger log = LoggerFactory.getLogger(BatteryManagementInstructionService.class);

    private BatteryManagementInstructionRepository batteryManagementInstructionRepository;
    private BatteryManagementInstructionSearchRepository batteryManagementInstructionSearchRepository;

    public BatteryManagementInstructionService(BatteryManagementInstructionRepository batteryManagementInstructionRepository,
                                               BatteryManagementInstructionSearchRepository batteryManagementInstructionSearchRepository) {
        this.batteryManagementInstructionRepository = batteryManagementInstructionRepository;
        this.batteryManagementInstructionSearchRepository = batteryManagementInstructionSearchRepository;
    }

    /**
     * Save a BatteryManagementInstruction.
     *
     * @param batteryManagementInstruction the entity to save
     * @return the persisted entity
     */
    public BatteryManagementInstruction save(BatteryManagementInstruction batteryManagementInstruction) {
        log.debug("Request to save BatteryManagementInstruction : {}", batteryManagementInstruction);
        BatteryManagementInstruction result = batteryManagementInstructionRepository.save(batteryManagementInstruction);
        batteryManagementInstructionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the BatteryManagementInstructions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<BatteryManagementInstruction> findAll() {
        log.debug("Request to get all BatteryManagementInstructions");
        return batteryManagementInstructionRepository.findAll();
    }


    /**
     * Get one BatteryManagementInstruction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BatteryManagementInstruction findOne(Long id) {
        log.debug("Request to get BatteryManagementInstruction : {}", id);
        return batteryManagementInstructionRepository.findOne(id);
    }


    /**
     * Delete one BatteryManagementInstruction
     *
     * @param instructionId the id of the BatteryManagementInstruction entity
     */
    public void delete(Long instructionId) {
        log.debug("Request to delete BatteryManagementInstruction {}", instructionId);
        batteryManagementInstructionRepository.delete(instructionId);
        batteryManagementInstructionSearchRepository.delete(instructionId);
    }

}
