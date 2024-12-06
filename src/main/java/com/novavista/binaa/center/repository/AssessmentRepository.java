package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Assessment;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.enums.AssessmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCaseInfo(Case caseInfo);
    List<Assessment> findByStatus(AssessmentStatus status);
}