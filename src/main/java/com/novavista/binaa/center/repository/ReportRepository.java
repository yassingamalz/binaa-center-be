package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCaseInfo(Case caseInfo);
}