package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.service.dto.FluxNodeDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;


@Service
public class FluxNodeMapper {

    private final EnergyProviderMapper energyProviderMapper;

    @Inject
    public FluxNodeMapper(EnergyProviderMapper energyProviderMapper) {
        this.energyProviderMapper = energyProviderMapper;
    }

    public FluxNodeDTO toDto(FluxNode fluxNode) {
        if (fluxNode == null) {
            return null;
        }
        FluxNodeDTO dto = new FluxNodeDTO();
        dto.setId(fluxNode.getId());
        dto.setName(fluxNode.getName());
        dto.setType(fluxNode.getType());

        // dto.setStateFluxNodeDTO(fluxNode.getStateFluxNodeDTO());
        // dto.setChildren(fluxNode.getChildren().stream().map(fluxNodeMapper::toDto).collect(Collectors.toSet()));
        // dto.setFluxTopologyDTO(fluxTopologyMapper.toDto(fluxNode.getFluxTopology()));

        return dto;
    }

    public FluxNodeDTO toDtoWithOutParent(FluxNode fluxNode) {
        if (fluxNode == null) {
            return null;
        }
        FluxNodeDTO dto = new FluxNodeDTO();
        dto.setId(fluxNode.getId());
        dto.setName(fluxNode.getName());
        dto.setType(fluxNode.getType());

        // dto.setStateFluxNodeDTO(fluxNode.getStateFluxNodeDTO());
        // dto.setChildren(fluxNode.getChildren().stream().map(fluxNodeMapper::toDto).collect(Collectors.toSet()));
        //  dto.setFluxTopologyDTO(fluxTopologyMapper.toDto(fluxNode.getFluxTopology()));

        return dto;
    }

    /*Fixme*/
    public FluxNode toEntity(FluxNodeDTO dto) {
        FluxNode entity = null;
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        //entity.setEnergyProviders(dto.getProviders().stream().map(c->energyProviderMapper.toEntity(c)).collect(Collectors.toSet()));
        return entity;
    }
}
