package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.FidelityPointsDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.FidelityPoints;
import com.novavista.binaa.center.enums.PointsStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.FidelityPointsRepository;
import com.novavista.binaa.center.services.FidelityPointsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class FidelityPointsServiceImpl implements FidelityPointsService {
    private final FidelityPointsRepository fidelityPointsRepository;
    private final CaseRepository caseRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FidelityPointsServiceImpl(FidelityPointsRepository fidelityPointsRepository,
                                     CaseRepository caseRepository,
                                     ModelMapper modelMapper) {
        this.fidelityPointsRepository = fidelityPointsRepository;
        this.caseRepository = caseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FidelityPointsDTO createPoints(FidelityPointsDTO pointsDTO) {
        log.info("Creating points transaction for case ID: {}", pointsDTO.getCaseId());
        validatePoints(pointsDTO);

        Case caseEntity = caseRepository.findById(pointsDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        FidelityPoints points = modelMapper.map(pointsDTO, FidelityPoints.class);
        points.setCaseInfo(caseEntity);
        points.setTransactionDate(LocalDateTime.now());

        FidelityPoints savedPoints = fidelityPointsRepository.save(points);
        log.info("Created points transaction with ID: {}", savedPoints.getPointId());
        return modelMapper.map(savedPoints, FidelityPointsDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public FidelityPointsDTO getPointsById(Long id) {
        return fidelityPointsRepository.findById(id)
                .map(points -> modelMapper.map(points, FidelityPointsDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Points transaction not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FidelityPointsDTO> getPointsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return fidelityPointsRepository.findByCaseInfo(caseEntity).stream()
                .map(points -> modelMapper.map(points, FidelityPointsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FidelityPointsDTO> getPointsByStatus(PointsStatus status) {
        return fidelityPointsRepository.findByStatus(status).stream()
                .map(points -> modelMapper.map(points, FidelityPointsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FidelityPointsDTO updatePoints(Long id, FidelityPointsDTO pointsDTO) {
        FidelityPoints existingPoints = fidelityPointsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Points transaction not found"));
        validatePoints(pointsDTO);
        modelMapper.map(pointsDTO, existingPoints);
        existingPoints.setPointId(id);

        FidelityPoints updatedPoints = fidelityPointsRepository.save(existingPoints);
        log.info("Updated points transaction ID: {}", id);
        return modelMapper.map(updatedPoints, FidelityPointsDTO.class);
    }

    @Override
    public void deletePoints(Long id) {
        try {
            fidelityPointsRepository.deleteById(id);
            log.info("Deleted points transaction ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete points transaction");
        }
    }

    private void validatePoints(FidelityPointsDTO pointsDTO) {
        if (pointsDTO.getPoints() == null || pointsDTO.getPoints() <= 0) {
            throw new ValidationException("Valid points value is required");
        }
        if (pointsDTO.getType() == null) {
            throw new ValidationException("Transaction type is required");
        }
        if (pointsDTO.getExpiryDate() != null && pointsDTO.getExpiryDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Expiry date cannot be in the past");
        }
    }
}