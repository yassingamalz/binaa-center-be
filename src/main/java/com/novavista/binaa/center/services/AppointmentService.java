package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.AppointmentDTO;
import com.novavista.binaa.center.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    List<AppointmentDTO> getAppointmentsByDateTime(LocalDateTime dateTime);
    List<AppointmentDTO> getAppointmentsByStatus(AppointmentStatus status);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
}
