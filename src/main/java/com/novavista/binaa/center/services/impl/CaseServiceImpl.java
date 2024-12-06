package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.enums.CaseStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.services.CaseService;
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
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CaseServiceImpl(CaseRepository caseRepository, ModelMapper modelMapper) {
        this.caseRepository = caseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CaseDTO createCase(CaseDTO caseDTO) {
        log.info("Creating new case for: {}", caseDTO.getName());
        validateNewCase(caseDTO);

        Case caseEntity = modelMapper.map(caseDTO, Case.class);
        Case savedCase = caseRepository.save(caseEntity);
        log.info("Case created with ID: {}", savedCase.getCaseId());
        return modelMapper.map(savedCase, CaseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CaseDTO getCaseById(Long id) {
        return caseRepository.findById(id)
                .map(caseEntity -> modelMapper.map(caseEntity, CaseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseDTO> getAllActiveCases() {
        return caseRepository.findByStatus(CaseStatus.ACTIVE).stream()
                .map(caseEntity -> modelMapper.map(caseEntity, CaseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseDTO> searchCasesByName(String name) {
        return caseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(caseEntity -> modelMapper.map(caseEntity, CaseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CaseDTO updateCase(Long id, CaseDTO caseDTO) {
        Case existingCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        modelMapper.map(caseDTO, existingCase);
        existingCase.setCaseId(id);

        Case updatedCase = caseRepository.save(existingCase);
        log.info("Case updated with ID: {}", id);
        return modelMapper.map(updatedCase, CaseDTO.class);
    }

    @Override
    public void deleteCase(Long id) {
        try {
            caseRepository.deleteById(id);
            log.info("Case deleted with ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete case: {}", e.getMessage());
            throw new RuntimeException("Cannot delete case due to existing references");
        }
    }

    private void validateNewCase(CaseDTO caseDTO) {
        if (caseDTO.getName() == null || caseDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Case name cannot be empty");
        }
    }
}