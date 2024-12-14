package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.TreatmentPlanDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.entity.TreatmentPlan;
import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.repository.TreatmentPlanRepository;
import com.novavista.binaa.center.services.TreatmentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TreatmentPlanServiceImpl implements TreatmentPlanService {
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TreatmentPlanServiceImpl(TreatmentPlanRepository treatmentPlanRepository,
                                    CaseRepository caseRepository,
                                    StaffRepository staffRepository,
                                    ModelMapper modelMapper) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TreatmentPlanDTO createTreatmentPlan(TreatmentPlanDTO planDTO) {
        log.info("Creating treatment plan for case ID: {}", planDTO.getCaseId());
        validateTreatmentPlan(planDTO);

        Case caseEntity = caseRepository.findById(planDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff specialist = staffRepository.findById(planDTO.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        TreatmentPlan plan = modelMapper.map(planDTO, TreatmentPlan.class);
        plan.setCaseInfo(caseEntity);
        plan.setSpecialist(specialist);

        TreatmentPlan savedPlan = treatmentPlanRepository.save(plan);
        log.info("Created treatment plan with ID: {}", savedPlan.getPlanId());
        return modelMapper.map(savedPlan, TreatmentPlanDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public TreatmentPlanDTO getTreatmentPlanById(Long id) {
        return treatmentPlanRepository.findById(id)
                .map(plan -> modelMapper.map(plan, TreatmentPlanDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> getTreatmentPlansByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return treatmentPlanRepository.findByCaseInfo(caseEntity).stream()
                .map(plan -> modelMapper.map(plan, TreatmentPlanDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> getTreatmentPlansByStatus(TreatmentPlanStatus status) {
        return treatmentPlanRepository.findByStatus(status).stream()
                .map(plan -> modelMapper.map(plan, TreatmentPlanDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TreatmentPlanDTO updateTreatmentPlan(Long id, TreatmentPlanDTO planDTO) {
        TreatmentPlan existingPlan = treatmentPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found"));
        validateTreatmentPlan(planDTO);
        modelMapper.map(planDTO, existingPlan);
        existingPlan.setPlanId(id);

        TreatmentPlan updatedPlan = treatmentPlanRepository.save(existingPlan);
        log.info("Updated treatment plan ID: {}", id);
        return modelMapper.map(updatedPlan, TreatmentPlanDTO.class);
    }

    @Override
    public void deleteTreatmentPlan(Long id) {
        try {
            treatmentPlanRepository.deleteById(id);
            log.info("Deleted treatment plan ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete treatment plan");
        }
    }

    private void validateTreatmentPlan(TreatmentPlanDTO planDTO) {
        if (planDTO.getGoals() == null || planDTO.getGoals().trim().isEmpty()) {
            throw new ValidationException("Treatment goals are required");
        }
        if (planDTO.getStartDate() == null) {
            throw new ValidationException("Start date is required");
        }
        if (planDTO.getEndDate() != null && planDTO.getEndDate().isBefore(planDTO.getStartDate())) {
            throw new ValidationException("End date cannot be before start date");
        }
    }
}
