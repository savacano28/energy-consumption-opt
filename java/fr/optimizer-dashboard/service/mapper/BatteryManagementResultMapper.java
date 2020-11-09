package fr.ifpen.synergreen.service.mapper;


import fr.ifpen.synergreen.domain.BatteryManagementResult;
import fr.ifpen.synergreen.domain.BatteryManagementRun;
import fr.ifpen.synergreen.service.BatteryManagementRunService;
import fr.ifpen.synergreen.service.dto.BatteryManagementResultDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.instantToInstructionDate;

@Service
public class BatteryManagementResultMapper {

    private final BatteryManagementInstructionMapper batteryManagementInstructionMapper;
    private final BatteryManagementRunService batteryManagementRunService;

    @Inject
    public BatteryManagementResultMapper(BatteryManagementInstructionMapper batteryManagementInstructionMapper,
                                         BatteryManagementRunService batteryManagementRunService) {
        this.batteryManagementInstructionMapper = batteryManagementInstructionMapper;
        this.batteryManagementRunService = batteryManagementRunService;
    }

    public BatteryManagementResultDTO toDto(BatteryManagementResult batteryManagementResult) {
        BatteryManagementResultDTO dto = new BatteryManagementResultDTO();
        dto.setRunId(batteryManagementResult.getRunId());
        dto.setStatus(batteryManagementResult.getStatus());
        dto.setErrorMsg(batteryManagementResult.getError());
        dto.setStartComputation(instantToInstructionDate(batteryManagementResult.getStart()));
        dto.setEndComputation(instantToInstructionDate(batteryManagementResult.getEnd()));
        if (!batteryManagementResult.getResult().isEmpty()) {
            dto.setResult(batteryManagementResult.getResult().stream().map(batteryManagementInstructionMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public BatteryManagementResultDTO toDto(BatteryManagementRun batteryManagementRun) {
        BatteryManagementResultDTO dto = new BatteryManagementResultDTO();
        dto.setRunId(batteryManagementRun.getId());
        dto.setStatus(batteryManagementRun.getStatus());
        dto.setErrorMsg(batteryManagementRun.getError());
        dto.setStartComputation(instantToInstructionDate(batteryManagementRun.getStart()));
        dto.setEndComputation(instantToInstructionDate(batteryManagementRun.getEnd()));
        if (!batteryManagementRun.getInstructions().isEmpty()) {
            dto.setResult(batteryManagementRun.getInstructions().stream().map(batteryManagementInstructionMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }
//
//    public BatteryManagementResultDTO toDto(BatteryManagementRun batteryManagementRun,
//                                            List<BatteryManagementInstruction> listOfInstructions) {
//        BatteryManagementResultDTO dto = new BatteryManagementResultDTO();
//        dto.setRunId(batteryManagementRun.getId());
//        dto.setStatus(batteryManagementRun.getStatus());
//        dto.setError(batteryManagementRun.getError());
//        dto.setStart(instantToInstructionDate(batteryManagementRun.getStart()));
//        dto.setEnd(instantToInstructionDate(batteryManagementRun.getEnd()));
//        if (listOfInstructions != null) {
//            dto.setResult(listOfInstructions.stream().map(batteryManagementInstructionMapper::toDto).collect(Collectors.toList()));
//        }
//        return dto;
//    }
}
