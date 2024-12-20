package com.novavista.binaa.center.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novavista.binaa.center.dto.request.NotificationDTO;
import com.novavista.binaa.center.entity.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationMapper implements EntityMapper<NotificationDTO, Notification> {
    private final ModelMapper mapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationMapper(ModelMapper mapper) {
        this.mapper = mapper;
        this.objectMapper = new ObjectMapper();
        
        // Configure ModelMapper for data field
        mapper.createTypeMap(Notification.class, NotificationDTO.class)
            .addMappings(m -> m.using(ctx -> parseDataToMap((String) ctx.getSource()))
                .map(Notification::getData, NotificationDTO::setData));
            
        mapper.createTypeMap(NotificationDTO.class, Notification.class)
            .addMappings(m -> m.using(ctx -> convertMapToString((Map<String, Object>) ctx.getSource()))
                .map(NotificationDTO::getData, Notification::setData));
    }

    @Override
    public NotificationDTO toDto(Notification entity) {
        return mapper.map(entity, NotificationDTO.class);
    }

    @Override
    public Notification toEntity(NotificationDTO dto) {
        return mapper.map(dto, Notification.class);
    }

    @Override
    public List<NotificationDTO> toDtoList(List<Notification> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> toEntityList(List<NotificationDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private String convertMapToString(Map<String, Object> data) {
        try {
            return data != null ? objectMapper.writeValueAsString(data) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON string", e);
        }
    }

    private Map<String, Object> parseDataToMap(String data) {
        try {
            return data != null ? objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {}) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON string to map", e);
        }
    }
}