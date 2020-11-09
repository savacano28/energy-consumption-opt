package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.BatteryManagementPrediction;
import fr.ifpen.synergreen.service.dto.BatteryManagementPredictionDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.instantToInstructionDate;
import static fr.ifpen.synergreen.service.util.StringFormatUtils.instructionDateToInstant;


@Service
public class BatteryManagementPredictionMapper {

    @Inject
    public BatteryManagementPredictionMapper() {
    }

    public BatteryManagementPredictionDTO toDto(BatteryManagementPrediction batteryManagementPrediction) {
        if (batteryManagementPrediction == null) {
            return null;
        }

        BatteryManagementPredictionDTO dto = new BatteryManagementPredictionDTO();
        dto.setDate(instantToInstructionDate(batteryManagementPrediction.getInstant()));
        dto.setPPV(batteryManagementPrediction.getpProdGlobal());
        dto.setConso(batteryManagementPrediction.getpConsoGlobal());
        dto.setPrixAchat(batteryManagementPrediction.getPrixAchat());
        dto.setPrixVente(batteryManagementPrediction.getPrixVente());

        return dto;
    }

    public BatteryManagementPrediction toEntity(BatteryManagementPredictionDTO dto) {
        BatteryManagementPrediction entity = new BatteryManagementPrediction();
        entity.setInstant(instructionDateToInstant(dto.getDate()));
        entity.setpProdGlobal(dto.getPPV());
        entity.setpConsoGlobal(dto.getConso());
        entity.setPrixAchat(dto.getPrixAchat());
        entity.setPrixVente(dto.getPrixVente());

        return entity;
    }

}
