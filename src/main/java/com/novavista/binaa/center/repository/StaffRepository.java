package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.dto.lookup.StaffLookupDTO;
import com.novavista.binaa.center.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByRole(String role);
    @Query("""
        SELECT new com.novavista.binaa.center.dto.lookup.StaffLookupDTO(
            s.id,
            s.name
        )
        FROM Staff s
        ORDER BY s.name ASC
    """)
    List<StaffLookupDTO > findAvailableStaffForLookup();
}