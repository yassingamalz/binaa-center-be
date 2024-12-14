package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.request.SessionDTO;
import com.novavista.binaa.center.dto.response.SessionResponseDTO;
import com.novavista.binaa.center.entity.Session;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper implements EntityMapper<SessionDTO, Session> {
    private final ModelMapper mapper;

    @Autowired
    public SessionMapper(ModelMapper mapper) {
        this.mapper = mapper;

        mapper.createTypeMap(Session.class, SessionResponseDTO.class)
                .addMappings(mapping -> {
                    mapping.map(src -> src.getCaseInfo().getName(), SessionResponseDTO::setCaseName);
                    mapping.map(src -> src.getStaff().getName(), SessionResponseDTO::setStaffName);
                });
    }

    public SessionResponseDTO toResponseDto(Session entity) {
        return mapper.map(entity, SessionResponseDTO.class);
    }

    public List<SessionResponseDTO> toResponseDtoList(List<Session> entityList) {
        return entityList.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SessionDTO toDto(Session entity) {
        return mapper.map(entity, SessionDTO.class);
    }

    @Override
    public Session toEntity(SessionDTO dto) {
        return mapper.map(dto, Session.class);
    }

    @Override
    public List<SessionDTO> toDtoList(List<Session> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Session> toEntityList(List<SessionDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}