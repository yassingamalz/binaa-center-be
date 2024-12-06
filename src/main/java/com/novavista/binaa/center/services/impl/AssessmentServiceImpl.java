package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.AssessmentDTO;
import com.novavista.binaa.center.entity.Assessment;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.AssessmentStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.AssessmentRepository;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.AssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository,
                                 CaseRepository caseRepository,
                                 StaffRepository staffRepository,
                                 ModelMapper modelMapper) {
        this.assessmentRepository = assessmentRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AssessmentDTO createAssessment(AssessmentDTO assessmentDTO) {
        log.info("Creating new assessment for case ID: {}", assessmentDTO.getCaseId());
        validateAssessment(assessmentDTO);

        Case caseEntity = caseRepository.findById(assessmentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff assessor = staffRepository.findById(assessmentDTO.getAssessorId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessor not found"));

        Assessment assessment = modelMapper.map(assessmentDTO, Assessment.class);
        assessment.setCaseInfo(caseEntity);
        assessment.setAssessor(assessor);

        Assessment savedAssessment = assessmentRepository.save(assessment);
        log.info("Created assessment with ID: {}", savedAssessment.getAssessmentId());
        return modelMapper.map(savedAssessment, AssessmentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDTO getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .map(assessment -> modelMapper.map(assessment, AssessmentDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentDTO> getAssessmentsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return assessmentRepository.findByCaseInfo(caseEntity).stream()
                .map(assessment -> modelMapper.map(assessment, AssessmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentDTO> getAssessmentsByStatus(AssessmentStatus status) {
        return assessmentRepository.findByStatus(status).stream()
                .map(assessment -> modelMapper.map(assessment, AssessmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AssessmentDTO updateAssessment(Long id, AssessmentDTO assessmentDTO) {
        log.info("Updating assessment ID: {}", id);
        Assessment existingAssessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found"));

        validateAssessment(assessmentDTO);
        modelMapper.map(assessmentDTO, existingAssessment);
        existingAssessment.setAssessmentId(id);

        Assessment updatedAssessment = assessmentRepository.save(existingAssessment);
        log.info("Updated assessment ID: {}", id);
        return modelMapper.map(updatedAssessment, AssessmentDTO.class);
    }

    @Override
    public void deleteAssessment(Long id) {
        log.info("Deleting assessment ID: {}", id);
        try {
            assessmentRepository.deleteById(id);
            log.info("Deleted assessment ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete assessment: {}", e.getMessage());
            throw new RuntimeException("Cannot delete assessment");
        }
    }

    private void validateAssessment(AssessmentDTO assessmentDTO) {
        if (assessmentDTO.getAssessmentType() == null) {
            throw new ValidationException("Assessment type is required");
        }
        if (assessmentDTO.getAssessmentDate() == null) {
            throw new ValidationException("Assessment date is required");
        }
        if (assessmentDTO.getAssessmentDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Assessment date cannot be in the future");
        }
    }
}