package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.entity.Case;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CaseMapper implements EntityMapper<CaseDTO, Case> {
    private final ModelMapper mapper;

    @Autowired
    public CaseMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CaseDTO toDto(Case entity) {
        return mapper.map(entity, CaseDTO.class);
    }

    @Override
    public Case toEntity(CaseDTO dto) {
        return mapper.map(dto, Case.class);
    }

    @Override
    public List<CaseDTO> toDtoList(List<Case> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Case> toEntityList(List<CaseDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}