package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.enums.CaseStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.mapper.CaseMapper;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.services.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;
    private final CaseMapper caseMapper;

    @Autowired
    public CaseServiceImpl(CaseRepository caseRepository, CaseMapper caseMapper) {
        this.caseRepository = caseRepository;
        this.caseMapper = caseMapper;
    }

    @Override
    public CaseDTO createCase(CaseDTO caseDTO) {
        log.info("Creating new case for: {}", caseDTO.getName());
        validateNewCase(caseDTO);

        // Set admission date to current date if not provided
        if (caseDTO.getAdmissionDate() == null) {
            caseDTO.setAdmissionDate(LocalDate.now());
        }

        Case caseEntity = caseMapper.toEntity(caseDTO);
        Case savedCase = caseRepository.save(caseEntity);
        log.info("Case created with ID: {}", savedCase.getCaseId());
        return caseMapper.toDto(savedCase);
    }

    @Override
    @Transactional(readOnly = true)
    public CaseDTO getCaseById(Long id) {
        return caseRepository.findById(id)
                .map(caseEntity -> caseMapper.toDto(caseEntity))
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseDTO> getAllCases() {
        log.info("Fetching all cases");
        return caseRepository.findAll().stream()
                .map(caseEntity -> caseMapper.toDto(caseEntity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseDTO> getCasesByStatus(String status) {
        log.info("Fetching cases with status: {}", status);
        CaseStatus caseStatus;
        try {
            caseStatus = CaseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid case status: {}", status);
            throw new ValidationException("Invalid case status: " + status);
        }

        return caseRepository.findByStatus(caseStatus).stream()
                .map(caseEntity -> caseMapper.toDto(caseEntity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaseDTO> getAllActiveCases() {
        return caseRepository.findByStatus(CaseStatus.ACTIVE).stream()
                .map(caseEntity -> caseMapper.toDto(caseEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseDTO> searchCasesByName(String name) {
        return caseRepository.findByNameContainingIgnoreCase(name).stream()
                .map(caseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CaseDTO updateCase(Long id, CaseDTO caseDTO) {
        Case existingCase = caseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        // Partial update logic: update only the provided fields
        if (caseDTO.getName() != null) {
            existingCase.setName(caseDTO.getName());
        }
        if (caseDTO.getAge() != null) {
            existingCase.setAge(caseDTO.getAge());
        }
        if (caseDTO.getGuardianName() != null) {
            existingCase.setGuardianName(caseDTO.getGuardianName());
        }
        if (caseDTO.getContactNumber() != null) {
            if (!caseDTO.getContactNumber().matches("^01[0125][0-9]{8}$")) {
                throw new ValidationException("Invalid contact number format");
            }
            existingCase.setContactNumber(caseDTO.getContactNumber());
        }
        if (caseDTO.getSpecialNeeds() != null) {
            existingCase.setSpecialNeeds(caseDTO.getSpecialNeeds());
        }
        if (caseDTO.getEmergencyContact() != null) {
            if (!caseDTO.getEmergencyContact().matches("^01[0125][0-9]{8}$")) {
                throw new ValidationException("Invalid emergency contact format");
            }
            existingCase.setEmergencyContact(caseDTO.getEmergencyContact());
        }
        if (caseDTO.getAdmissionDate() != null) {
            existingCase.setAdmissionDate(caseDTO.getAdmissionDate());
        }
        if (caseDTO.getStatus() != null) {
            try {
                CaseStatus newStatus = CaseStatus.valueOf(caseDTO.getStatus().name());
                existingCase.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid case status: " + caseDTO.getStatus());
            }
        }

        // Save the updated case
        Case updatedCase = caseRepository.save(existingCase);
        log.info("Case updated with ID: {}", id);
        return caseMapper.toDto(updatedCase);
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

        // Add validation for other required fields
        if (caseDTO.getAge() == null) {
            throw new ValidationException("Age cannot be empty");
        }

        if (caseDTO.getGuardianName() == null || caseDTO.getGuardianName().trim().isEmpty()) {
            throw new ValidationException("Guardian name cannot be empty");
        }

        if (caseDTO.getContactNumber() == null || caseDTO.getContactNumber().trim().isEmpty()) {
            throw new ValidationException("Contact number cannot be empty");
        }

        // Set default status if not provided
        if (caseDTO.getStatus() == null) {
            caseDTO.setStatus(CaseStatus.ACTIVE);
        }
    }

}