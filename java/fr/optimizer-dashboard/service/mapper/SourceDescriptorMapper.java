package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.SourceDescriptor;
import fr.ifpen.synergreen.service.dto.SourceDescriptorDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SourceDescriptorMapper {

    private final ParameterMapper parameterMapper;

    @Inject
    public SourceDescriptorMapper(ParameterMapper parameterMapper) {
        this.parameterMapper = parameterMapper;
    }


    public SourceDescriptorDTO toDTO(SourceDescriptor sourceDescriptor) {
        if (sourceDescriptor == null) {
            return null;
        }
        SourceDescriptorDTO dto = new SourceDescriptorDTO();
        dto.setId(sourceDescriptor.getId());
        dto.setLabel(sourceDescriptor.getLabel());
        dto.setSource(sourceDescriptor.getSource());
        dto.setDescriptors(sourceDescriptor.getDescriptors().stream().map(p -> parameterMapper.toDtoWithOutValues(p)).collect(Collectors.toList()));
        return dto;
    }


}
