package fr.ifpen.synergreen.service.mapper;


import fr.ifpen.synergreen.domain.EnergySite;
import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.domain.FluxTopology;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.service.dto.EnergySiteDTO;
import fr.ifpen.synergreen.service.dto.FluxTopologyDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FluxTopologyMapper {

    private final EnergyProviderMapper energyProviderMapper;
    private final FluxNodeMapper fluxNodeMapper;
    private final InvoiceMapper invoiceMapper;
    private final int IMAGE_MAX_SIZE = 150;

    @Inject
    public FluxTopologyMapper(EnergyProviderMapper energyProviderMapper, FluxNodeMapper fluxNodeMapper, InvoiceMapper invoiceMapper) {
        this.fluxNodeMapper = fluxNodeMapper;
        this.energyProviderMapper = energyProviderMapper;
        this.invoiceMapper = invoiceMapper;
    }

    public FluxTopologyDTO toSimpleDTO(FluxTopology fluxTopology) {
        if (fluxTopology == null) {
            return null;
        }
        FluxTopologyDTO dto = new FluxTopologyDTO();
        dto.setId(fluxTopology.getId());
        dto.setName(fluxTopology.getName());
        dto.setInvoices(fluxTopology.getInvoices().stream().map(i -> invoiceMapper.toDto(i)).collect(Collectors.toList()));
        dto.setImg(fluxTopology.getImg());
        return dto;
    }


    public FluxTopologyDTO toDto(FluxTopology fluxTopology) {
        if (fluxTopology == null) {
            return null;
        }
        FluxTopologyDTO dto = new FluxTopologyDTO();
        dto.setId(fluxTopology.getId());
        dto.setName(fluxTopology.getName());
        dto.setOptimization(fluxTopology.isOptimization());
        dto.setOptimizations(fluxTopology.getOptimizations().stream().map(this::toDtoWithOutParent).collect(Collectors.toSet()));
        dto.setFluxNodes(fluxTopology.getChildren().stream().map(fluxNodeMapper::toDto).collect(Collectors.toSet()));

        /*Get all elements children from a topology*/
        List<FluxNode> children = new ArrayList<>();
        children.addAll(
            fluxTopology.getChildren().stream()
                .map(FluxNode::getAllChildren)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        dto.setAllElementsChildren(children.stream().filter(c -> !c.getType().equals(FluxNodeType.GROUP)).map(fluxNodeMapper::toDtoWithOutParent).collect(Collectors.toList()));
        dto.setProvider(energyProviderMapper.toDto(fluxTopology.getEnergyProvider()));
        dto.setStateFluxNodeDTO(fluxTopology.getStateFluxNodeDTO());
        dto.setInvoices(fluxTopology.getInvoices().stream().map(i -> invoiceMapper.toDto(i)).collect(Collectors.toList()));
        dto.setImg(fluxTopology.getImg());

        final EnergySiteDTO energySite = new EnergySiteDTO();
        energySite.setId(fluxTopology.getEnergySite().getId());
        energySite.setName(fluxTopology.getEnergySite().getName());
        dto.setEnergySite(energySite);

        return dto;
    }

    public FluxTopologyDTO toDtoWithOutParent(FluxTopology fluxTopology) {
        if (fluxTopology == null) {
            return null;
        }
        FluxTopologyDTO dto = new FluxTopologyDTO();
        dto.setId(fluxTopology.getId());
        dto.setName(fluxTopology.getName());
        dto.setOptimization(fluxTopology.isOptimization());
        dto.setFluxNodes(fluxTopology.getChildren().stream().map(fluxNodeMapper::toDto).collect(Collectors.toSet()));
        dto.setProvider(energyProviderMapper.toDto(fluxTopology.getEnergyProvider()));
        dto.setInvoices(fluxTopology.getInvoices().stream().map(i -> invoiceMapper.toDto(i)).collect(Collectors.toList()));
        dto.setImg(fluxTopology.getImg());
        return dto;
    }

    public FluxTopologyDTO toDtoWithAllChildren(FluxTopology fluxTopology) {
        if (fluxTopology == null) {
            return null;
        }
        FluxTopologyDTO dto = new FluxTopologyDTO();
        dto.setId(fluxTopology.getId());
        dto.setName(fluxTopology.getName());
        dto.setOptimization(fluxTopology.isOptimization());
        dto.setFluxNodes(fluxTopology.getChildren().stream().map(fluxNodeMapper::toDto).collect(Collectors.toSet()));
        //todo dto.setAllChildren(fluxTopology)
        dto.setProvider(energyProviderMapper.toDto(fluxTopology.getEnergyProvider()));
        dto.setInvoices(fluxTopology.getInvoices().stream().map(invoiceMapper::toDto).collect(Collectors.toList()));
        dto.setImg(fluxTopology.getImg());
        return dto;
    }

    public FluxTopology toEntity(FluxTopologyDTO dto) {
        FluxTopology entity = new FluxTopology();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setOptimization(dto.isOptimization());
        entity.setOptimizations(dto.getOptimizations().stream().map(this::toEntityWithOutOptimizations).collect(Collectors.toSet()));
        entity.setEnergyProvider(energyProviderMapper.toEntity(dto.getProvider()));
        entity.setInvoices(dto.getInvoices().stream().map(invoiceMapper::toEntity).collect(Collectors.toList()));
        entity.setImg(dto.getImg());
        final EnergySite energySite = new EnergySite();
        energySite.setId(dto.getEnergySite().getId());
        entity.setEnergySite(energySite);
        // entity.setChildren(dto.getChildren().stream().map(c->fluxNodeMapper.toEntity(c)).collect(Collectors.toList()));
        return entity;
    }

    public FluxTopology toEntityWithOutInvoices(FluxTopologyDTO dto) {
        FluxTopology entity = new FluxTopology();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setOptimization(dto.isOptimization());
        entity.setOptimizations(dto.getOptimizations().stream().map(this::toEntityWithOutOptimizations).collect(Collectors.toSet()));
        entity.setEnergyProvider(energyProviderMapper.toEntity(dto.getProvider()));
        entity.setImg(dto.getImg());
        final EnergySite energySite = new EnergySite();
        energySite.setId(dto.getEnergySite().getId());
        entity.setEnergySite(energySite);
        return entity;
    }

    public FluxTopology toEntityWithOutOptimizations(FluxTopologyDTO dto) {
        FluxTopology entity = new FluxTopology();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setOptimization(dto.isOptimization());
        entity.setImg(dto.getImg());
        return entity;
    }
}
