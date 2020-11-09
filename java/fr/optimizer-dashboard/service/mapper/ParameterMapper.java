package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.Parameter;
import fr.ifpen.synergreen.service.dto.ParameterDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;


@Service
public class ParameterMapper {


    @Inject
    public ParameterMapper() {
    }

    public ParameterDTO toDto(Parameter entity) {
        if (entity == null) {
            return null;
        }
        ParameterDTO dto = new ParameterDTO();
        dto.setId(entity.getId());
        dto.setParameterLabel(entity.getParameterLabel());
        dto.setParameterType(entity.getParameterType());
        dto.setParameterValue(entity.getParameterValue());
        return dto;
    }

    public ParameterDTO toDtoWithOutValues(Parameter entity) {
        if (entity == null) {
            return null;
        }
        ParameterDTO dto = new ParameterDTO();
        dto.setId(entity.getId());
        dto.setParameterLabel(entity.getParameterLabel());
        dto.setParameterType(entity.getParameterType());
        return dto;
    }

    /*public Parameter toEntity(ParameterDTO energyElementDTO) {
        Parameter entity = new Parameter();
        return entity;
    }*/

}
