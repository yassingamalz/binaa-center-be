package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT s FROM Session s " +
            "LEFT JOIN FETCH s.caseInfo c " +
            "LEFT JOIN FETCH s.staff st")
    List<Session> findAllWithDetails();

    @Query("SELECT s FROM Session s " +
            "LEFT JOIN FETCH s.caseInfo c " +
            "LEFT JOIN FETCH s.staff st " +
            "WHERE s.id = :id")
    Optional<Session> findByIdWithDetails(@Param("id") Long id);

    List<Session> findByCaseInfo(Case caseInfo);
    List<Session> findBySessionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Session> findByAttendanceStatus(AttendanceStatus status);
    List<Session> findByStaff(Staff staff);

    @Query("SELECT s FROM Session s " +
            "LEFT JOIN FETCH s.caseInfo c " +
            "LEFT JOIN FETCH s.staff st " +
            "WHERE s.caseInfo = :caseInfo")
    List<Session> findByCaseInfoWithDetails(@Param("caseInfo") Case caseInfo);

    @Query("SELECT s FROM Session s " +
            "LEFT JOIN FETCH s.caseInfo c " +
            "LEFT JOIN FETCH s.staff st " +
            "WHERE s.staff = :staff")
    List<Session> findByStaffWithDetails(@Param("staff") Staff staff);
}