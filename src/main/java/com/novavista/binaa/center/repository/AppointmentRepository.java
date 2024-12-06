package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Appointment;
import com.novavista.binaa.center.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDateTime(LocalDateTime dateTime);
    List<Appointment> findByStatus(AppointmentStatus status);
}
