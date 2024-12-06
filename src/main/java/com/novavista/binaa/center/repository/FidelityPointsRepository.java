package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.FidelityPoints;
import com.novavista.binaa.center.enums.PointsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FidelityPointsRepository extends JpaRepository<FidelityPoints, Long> {
    List<FidelityPoints> findByCaseInfo(Case caseInfo);
    List<FidelityPoints> findByStatus(PointsStatus status);
}