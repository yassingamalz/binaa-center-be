package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.DocumentDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Document;
import com.novavista.binaa.center.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper implements EntityMapper<DocumentDTO, Document> {
    private final ModelMapper mapper;
    
    @Autowired
    public DocumentMapper(ModelMapper mapper) {
        // Configure specific mappings for Document
        mapper.createTypeMap(Case.class, Long.class)
            .setConverter(context -> context.getSource() == null ? null : context.getSource().getCaseId());
            
        mapper.createTypeMap(User.class, Long.class)
            .setConverter(context -> context.getSource() == null ? null : context.getSource().getUserId());
            
        this.mapper = mapper;
    }
    
    @Override
    public DocumentDTO toDto(Document entity) {
        return mapper.map(entity, DocumentDTO.class);
    }
    
    @Override
    public Document toEntity(DocumentDTO dto) {
        return mapper.map(dto, Document.class);
    }
    
    @Override
    public List<DocumentDTO> toDtoList(List<Document> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Document> toEntityList(List<DocumentDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}