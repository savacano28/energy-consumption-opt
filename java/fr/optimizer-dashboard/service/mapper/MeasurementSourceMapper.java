package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.MeasurementSource;
import fr.ifpen.synergreen.service.dto.MeasurementSourceDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MeasurementSourceMapper {

    private final ParameterMapper parameterMapper;

    @Inject
    public MeasurementSourceMapper(ParameterMapper parameterMapper) {
        this.parameterMapper = parameterMapper;
    }


    public MeasurementSourceDTO toDTO(MeasurementSource measurementSource) {
        if (measurementSource == null) {
            return null;
        }
        MeasurementSourceDTO dto = new MeasurementSourceDTO();
        dto.setId(measurementSource.getId());
        dto.setParameters(measurementSource.getParameters().stream().map(p -> parameterMapper.toDto(p)).collect(Collectors.toList()));
        return dto;
    }


}
