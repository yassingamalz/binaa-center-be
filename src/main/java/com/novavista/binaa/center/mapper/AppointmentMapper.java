package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.request.AppointmentDTO;
import com.novavista.binaa.center.dto.response.AppointmentResponseDTO;
import com.novavista.binaa.center.entity.Appointment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper implements EntityMapper<AppointmentDTO, Appointment> {
    private final ModelMapper mapper;

    @Autowired
    public AppointmentMapper(ModelMapper mapper) {
        this.mapper = mapper;

        mapper.createTypeMap(Appointment.class, AppointmentResponseDTO.class)
                .addMappings(mapping -> {
                    mapping.map(src -> src.getCaseInfo().getName(), AppointmentResponseDTO::setCaseName);
                    mapping.map(src -> src.getStaff().getName(), AppointmentResponseDTO::setStaffName);
                });
    }

    public AppointmentResponseDTO toResponseDto(Appointment entity) {
        return mapper.map(entity, AppointmentResponseDTO.class);
    }

    @Override
    public AppointmentDTO toDto(Appointment entity) {
        return mapper.map(entity, AppointmentDTO.class);
    }

    @Override
    public Appointment toEntity(AppointmentDTO dto) {
        return mapper.map(dto, Appointment.class);
    }

    @Override
    public List<AppointmentDTO> toDtoList(List<Appointment> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> toEntityList(List<AppointmentDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}