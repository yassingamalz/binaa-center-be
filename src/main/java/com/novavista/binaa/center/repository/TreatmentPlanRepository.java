package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.TreatmentPlan;
import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    List<TreatmentPlan> findByCaseInfo(Case caseInfo);
    List<TreatmentPlan> findByStatus(TreatmentPlanStatus status);
}
