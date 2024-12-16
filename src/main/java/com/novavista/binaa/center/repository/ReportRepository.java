package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCaseInfo(Case caseInfo);

    @Query("SELECT r FROM Report r WHERE " +
            "(:caseId IS NULL OR r.caseInfo.id = :caseId) AND " +
            "(:reportType IS NULL OR r.reportType = :reportType) AND " +
            "(:startDate IS NULL OR r.createdDate >= :startDate) AND " +
            "(:endDate IS NULL OR r.createdDate <= :endDate)")
    Page<Report> findByFilters(
            @Param("caseId") Long caseId,
            @Param("reportType") String reportType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}