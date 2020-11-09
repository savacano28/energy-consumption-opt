package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.EnergySite;
import fr.ifpen.synergreen.service.dto.EnergySiteDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EnergySiteMapper {

    private final FluxTopologyMapper fluxTopologyMapper;
    private final EnergyManagementSystemMapper energyManagementSystemMapper;

    @Inject
    public EnergySiteMapper(FluxTopologyMapper fluxTopologyMapper,
                            @Lazy EnergyManagementSystemMapper energyManagementSystemMapper) {
        this.fluxTopologyMapper = fluxTopologyMapper;
        this.energyManagementSystemMapper = energyManagementSystemMapper;
    }

    public EnergySiteDTO toDto(EnergySite energySite) {
        if (energySite == null) {
            return null;
        }
        EnergySiteDTO dto = new EnergySiteDTO();
        dto.setId(energySite.getId());
        dto.setName(energySite.getName());
        dto.setEnergyManagementSystem(energyManagementSystemMapper.toDto(energySite.getEnergyManagementSystem()));
        dto.setFluxTopologies(energySite.getFluxTopologies().stream().map(fluxTopologyMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }


    public EnergySiteDTO toSimpleDto(EnergySite energySite) {
        if (energySite == null) {
            return null;
        }
        EnergySiteDTO dto = new EnergySiteDTO();
        dto.setName(energySite.getName());
        dto.setEnergyManagementSystem(energyManagementSystemMapper.toDto(energySite.getEnergyManagementSystem()));
        return dto;
    }

    public EnergySiteDTO toDtoWithOptimizations(EnergySite energySite) {
        if (energySite == null) {
            return null;
        }
        EnergySiteDTO dto = new EnergySiteDTO();
        dto.setId(energySite.getId());
        dto.setName(energySite.getName());
        dto.setEnergyManagementSystem(energyManagementSystemMapper.toDto(energySite.getEnergyManagementSystem()));
        dto.setFluxTopologies(energySite.getFluxTopologies().stream().filter(fluxTopology -> !fluxTopology.isOptimization()).map(fluxTopologyMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }

    public EnergySite toEntity(EnergySiteDTO dto) {
        EnergySite entity = new EnergySite();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEnergyManagementSystem(energyManagementSystemMapper.toEntity(dto.getEnergyManagementSystem()));
        entity.setFluxTopologies(dto.getFluxTopologies().stream().map(fluxTopologyMapper::toEntity).collect(Collectors.toSet()));
        return entity;

    }
}
