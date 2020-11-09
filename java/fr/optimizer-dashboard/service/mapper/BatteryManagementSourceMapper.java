package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.BatteryManagementSource;
import fr.ifpen.synergreen.service.dto.BatteryManagementSourceDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BatteryManagementSourceMapper {

    //TODO toEntity

    private final BatteryManagementPredictionMapper batteryManagementPredictionMapper;

    @Inject
    public BatteryManagementSourceMapper(BatteryManagementPredictionMapper batteryManagementPredictionMapper) {
        this.batteryManagementPredictionMapper = batteryManagementPredictionMapper;
    }

    public BatteryManagementSource toEntity(BatteryManagementSourceDTO dto) {
        BatteryManagementSource entity = new BatteryManagementSource();
        entity.setEm(dto.getEm());
        entity.setEp(dto.getEp());
        entity.setEpm(dto.getEpm());
        entity.setEpp(dto.getEpp());
        entity.setEm(dto.getEm());
        entity.setBatteryType(dto.getBatteryType());
        entity.setVecEPlim(dto.getVecEPlim());
        entity.setPcp(dto.getPcp());
        entity.setPdp(dto.getPdp());
        entity.setRhoC(dto.getRhoC());
        entity.setRhoD(dto.getRhoD());
        entity.setE0(dto.getE0());
        entity.setE0tfFactor(dto.getE0tfFactor());
        entity.setEtf(dto.getEtf());
        entity.setPs(dto.getPs());
        entity.setAlphaPeak(dto.getAlphaPeak());
        entity.setT(dto.getT());
        entity.setDT(dto.getDT());
        if (dto.getDataset() != null) {
            entity.setDataset(dto.getDataset().stream().map(batteryManagementPredictionMapper::toEntity).collect(Collectors.toList()));
        }

        return entity;
    }


    public BatteryManagementSourceDTO toDto(BatteryManagementSource entity) {
        BatteryManagementSourceDTO dto = new BatteryManagementSourceDTO();
        dto.setEm(entity.getEm());
        dto.setEp(entity.getEp());
        dto.setEpm(entity.getEpm());
        dto.setEpp(entity.getEpp());
        dto.setEm(entity.getEm());
        dto.setBatteryType(entity.getBatteryType());
        dto.setVecEPlim(entity.getVecEPlim());
        dto.setPcp(entity.getPcp());
        dto.setPdp(entity.getPdp());
        dto.setRhoC(entity.getRhoC());
        dto.setRhoD(entity.getRhoD());
        dto.setE0(entity.getE0());
        dto.setE0tfFactor(entity.getE0tfFactor());
        dto.setEtf(entity.getEtf());
        dto.setPs(entity.getPs());
        dto.setAlphaPeak(entity.getAlphaPeak());
        dto.setT(entity.getT());
        dto.setDT(entity.getDT());
        if (entity.getDataset() != null) {
            dto.setDataset(entity.getDataset().stream().map(batteryManagementPredictionMapper::toDto).collect(Collectors.toList()));
        }

        return dto;
    }
}
