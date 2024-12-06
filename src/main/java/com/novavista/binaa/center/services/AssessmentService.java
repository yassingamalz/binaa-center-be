package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.AssessmentDTO;
import com.novavista.binaa.center.enums.AssessmentStatus;

import java.util.List;
public interface AssessmentService {
    AssessmentDTO createAssessment(AssessmentDTO assessmentDTO);
    AssessmentDTO getAssessmentById(Long id);
    List<AssessmentDTO> getAssessmentsByCase(Long caseId);
    List<AssessmentDTO> getAssessmentsByStatus(AssessmentStatus status);
    AssessmentDTO updateAssessment(Long id, AssessmentDTO assessmentDTO);
    void deleteAssessment(Long id);
}
