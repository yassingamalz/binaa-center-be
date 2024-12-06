package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.FidelityPointsDTO;
import com.novavista.binaa.center.enums.PointsStatus;

import java.util.List;

public interface FidelityPointsService {
    FidelityPointsDTO createPoints(FidelityPointsDTO pointsDTO);
    FidelityPointsDTO getPointsById(Long id);
    List<FidelityPointsDTO> getPointsByCase(Long caseId);
    List<FidelityPointsDTO> getPointsByStatus(PointsStatus status);
    FidelityPointsDTO updatePoints(Long id, FidelityPointsDTO pointsDTO);
    void deletePoints(Long id);
}
