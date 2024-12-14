package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.dto.lookup.CaseLookupDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.enums.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    List<Case> findByStatus(CaseStatus status);
    List<Case> findByNameContainingIgnoreCase(String name);
    @Query("""
        SELECT new com.novavista.binaa.center.dto.lookup.CaseLookupDTO(
            c.id,
            c.name
        )
        FROM Case c
        WHERE c.status = 'ACTIVE'
        ORDER BY c.name ASC
    """)
    List<CaseLookupDTO> findActiveCasesForLookup();
}