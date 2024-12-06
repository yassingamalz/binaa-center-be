package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.TreatmentPlanDTO;
import com.novavista.binaa.center.enums.TreatmentPlanStatus;

import java.util.List;

public interface TreatmentPlanService {
    TreatmentPlanDTO createTreatmentPlan(TreatmentPlanDTO planDTO);
    TreatmentPlanDTO getTreatmentPlanById(Long id);
    List<TreatmentPlanDTO> getTreatmentPlansByCase(Long caseId);
    List<TreatmentPlanDTO> getTreatmentPlansByStatus(TreatmentPlanStatus status);
    TreatmentPlanDTO updateTreatmentPlan(Long id, TreatmentPlanDTO planDTO);
    void deleteTreatmentPlan(Long id);
}





