package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.EnergyManagementSystem;
import fr.ifpen.synergreen.service.dto.EnergyManagementSystemDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class EnergyManagementSystemMapper {

    private final EnergySiteMapper energySiteMapper;

    @Inject
    public EnergyManagementSystemMapper(EnergySiteMapper energySiteMapper) {
        this.energySiteMapper = energySiteMapper;
    }

    public EnergyManagementSystemDTO toDto(EnergyManagementSystem energyManagementSystem) {
        if (energyManagementSystem == null) return null;
        EnergyManagementSystemDTO dto = new EnergyManagementSystemDTO();
        dto.setName(energyManagementSystem.getName());
        dto.setId(energyManagementSystem.getId());
        return dto;
    }

    public EnergyManagementSystem toEntity(EnergyManagementSystemDTO dto) {
        EnergyManagementSystem entity = new EnergyManagementSystem();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
      /*  entity.setEnergySites(dto.getEnergySites().stream()
            .map(energySiteMapper::toEntity)
            .collect(Collectors.toSet()));*/
        return entity;
    }

}
