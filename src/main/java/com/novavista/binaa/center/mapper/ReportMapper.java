package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.request.ReportDTO;
import com.novavista.binaa.center.dto.request.ReportGenerationRequestDTO;
import com.novavista.binaa.center.dto.response.ReportResultDTO;
import com.novavista.binaa.center.entity.Report;
import com.novavista.binaa.center.enums.ReportStatus;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportMapper implements EntityMapper<ReportDTO, Report> {
    private final ModelMapper mapper;

    @Autowired
    public ReportMapper(ModelMapper mapper) {
        this.mapper = mapper;

        // Reset ModelMapper configuration
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        configureTypeMaps();
    }

    private void configureTypeMaps() {
        // Configure Report -> ReportDTO mapping
        mapper.typeMap(Report.class, ReportDTO.class)
                .addMapping(src -> src.getCaseInfo().getCaseId(), ReportDTO::setCaseId)
                .addMapping(src -> src.getSession() != null ? src.getSession().getSessionId() : null, ReportDTO::setSessionId)
                .addMapping(Report::getReportType, (dest, value) -> dest.setReportType(value != null ? value.toString() : null));

        // Configure ReportDTO -> Report mapping
        TypeMap<ReportDTO, Report> dtoToEntityMap = mapper.createTypeMap(ReportDTO.class, Report.class);
        dtoToEntityMap.addMappings(mapper -> {
            mapper.skip(Report::setCaseInfo);
            mapper.skip(Report::setSession);
            mapper.skip(Report::setCreatedBy);
        });

        // Configure Report -> ReportResultDTO mapping
        mapper.typeMap(Report.class, ReportResultDTO.class)
                .addMapping(src -> src.getCaseInfo().getCaseId(), ReportResultDTO::setCaseId)
                .addMapping(src -> src.getSession() != null ? src.getSession().getSessionId() : null, ReportResultDTO::setSessionId)
                .addMapping(Report::getReportType, (dest, value) -> dest.setReportType(value != null ? value.toString() : null));

        // Configure ReportGenerationRequestDTO -> Report mapping
        TypeMap<ReportGenerationRequestDTO, Report> requestMap = mapper.createTypeMap(ReportGenerationRequestDTO.class, Report.class);
        requestMap.addMappings(mapper -> {
            mapper.skip(Report::setReportId);
            mapper.skip(Report::setCaseInfo);
            mapper.skip(Report::setSession);
            mapper.skip(Report::setCreatedBy);
        });
    }

    public ReportResultDTO toResultDto(Report entity) {
        if (entity == null) return null;
        ReportResultDTO dto = mapper.map(entity, ReportResultDTO.class);
        dto.setStatus(ReportStatus.GENERATED);
        return dto;
    }

    public List<ReportResultDTO> toResultDtoList(List<Report> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toResultDto)
                .collect(Collectors.toList());
    }

    public Report fromGenerationRequest(ReportGenerationRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        Report report = mapper.map(requestDTO, Report.class);
        report.setCreatedDate(LocalDate.now());
        return report;
    }

    @Override
    public ReportDTO toDto(Report entity) {
        if (entity == null) return null;
        return mapper.map(entity, ReportDTO.class);
    }

    @Override
    public Report toEntity(ReportDTO dto) {
        if (dto == null) return null;
        return mapper.map(dto, Report.class);
    }

    @Override
    public List<ReportDTO> toDtoList(List<Report> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> toEntityList(List<ReportDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}