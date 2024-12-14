package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.dto.response.AppointmentResponseDTO;
import com.novavista.binaa.center.entity.Appointment;
import com.novavista.binaa.center.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStatus(AppointmentStatus status);

    @Query("""
        SELECT new com.novavista.binaa.center.dto.response.AppointmentResponseDTO(
            a.appointmentId, 
            a.caseInfo.id, 
            a.staff.id, 
            a.dateTime, 
            a.status, 
            a.type, 
            a.notes,
            c.name, 
            s.name)
        FROM Appointment a
        JOIN a.caseInfo c
        JOIN a.staff s
        """)
    List<AppointmentResponseDTO> findAllWithDetails();

    @Query("""
        SELECT new com.novavista.binaa.center.dto.response.AppointmentResponseDTO(
            a.appointmentId, 
            a.caseInfo.id, 
            a.staff.id, 
            a.dateTime, 
            a.status, 
            a.type, 
            a.notes,
            c.name, 
            s.name)
        FROM Appointment a
        JOIN a.caseInfo c
        JOIN a.staff s
        WHERE a.appointmentId = :id
        """)
    Optional<AppointmentResponseDTO> findByIdWithDetails(@Param("id") Long id);

    @Query("""
        SELECT new com.novavista.binaa.center.dto.response.AppointmentResponseDTO(
            a.appointmentId, 
            a.caseInfo.id, 
            a.staff.id, 
            a.dateTime, 
            a.status, 
            a.type, 
            a.notes,
            c.name, 
            s.name)
        FROM Appointment a
        JOIN a.caseInfo c
        JOIN a.staff s
        WHERE a.dateTime = :dateTime
        """)
    List<AppointmentResponseDTO> findByDateTimeWithDetails(@Param("dateTime") LocalDateTime dateTime);

    @Query("""
        SELECT new com.novavista.binaa.center.dto.response.AppointmentResponseDTO(
            a.appointmentId,
            a.caseInfo.id,
            a.staff.id,
            a.dateTime,
            a.status,
            a.type,
            a.notes,
            c.name,
            s.name)
        FROM Appointment a
        JOIN a.caseInfo c
        JOIN a.staff s
        WHERE a.status = :status
        """)
    List<AppointmentResponseDTO> findByStatusWithDetails(@Param("status") AppointmentStatus status);

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.staff.id = :staffId
        AND a.dateTime < :endTime
        AND a.dateTime >= :startTime
        AND (:excludeId IS NULL OR a.appointmentId != :excludeId)
        """)
    boolean isStaffBusy(
            @Param("staffId") Long staffId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );

    @Query("""
        SELECT COUNT(a) > 0
        FROM Appointment a
        WHERE a.caseInfo.id = :caseId
        AND a.dateTime < :endTime
        AND a.dateTime >= :startTime
        AND (:excludeId IS NULL OR a.appointmentId != :excludeId)
        """)
    boolean isCaseBusy(
            @Param("caseId") Long caseId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId
    );
}