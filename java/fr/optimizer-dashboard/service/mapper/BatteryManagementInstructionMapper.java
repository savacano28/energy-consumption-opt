package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.BatteryManagementInstruction;
import fr.ifpen.synergreen.service.dto.BatteryManagementInstructionDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.instantToInstructionDate;
import static fr.ifpen.synergreen.service.util.StringFormatUtils.instructionDateToInstant;


@Service
public class BatteryManagementInstructionMapper {

    @Inject
    public BatteryManagementInstructionMapper() {
    }

    public BatteryManagementInstructionDTO toDto(BatteryManagementInstruction entity) {
        if (entity == null) {
            return null;
        }

        BatteryManagementInstructionDTO dto = new BatteryManagementInstructionDTO();
        dto.setDate(instantToInstructionDate(entity.getInstant()));
        dto.setpProdGlobal(entity.getpProdGlobal());
        dto.setDeltapProdGlobal(entity.getDeltaProdGlobal());
        dto.setLambdaPV(entity.getLambdaProdGlobal());
        dto.setpConsoGlobal(entity.getpConsoGlobal());
        dto.setDeltapConsoGlobal(entity.getDeltaConsoGlobal());
        dto.setLambdapConsoGlobal(entity.getLambdaConsoGlobal());
        dto.setP_bat(entity.getpBat());
        dto.setSoc(entity.getSoc());

        return dto;
    }

    public BatteryManagementInstruction toEntity(BatteryManagementInstructionDTO dto) {
        BatteryManagementInstruction entity = new BatteryManagementInstruction();
        entity.setInstant(instructionDateToInstant(dto.getDate()));
        entity.setpProdGlobal(dto.getpProdGlobal());
        entity.setDeltaProdGlobal(dto.getDeltapProdGlobal());
        entity.setLambdaProdGlobal(dto.getLambdaPV());
        entity.setpConsoGlobal(dto.getpConsoGlobal());
        entity.setDeltaConsoGlobal(dto.getDeltapConsoGlobal());
        entity.setLambdaConsoGlobal(dto.getLambdapConsoGlobal());
        entity.setpBat(dto.getP_bat());
        entity.setSoc(dto.getSoc());

        return entity;
    }

}
