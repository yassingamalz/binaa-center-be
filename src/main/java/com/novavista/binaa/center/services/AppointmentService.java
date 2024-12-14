package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.AppointmentDTO;
import com.novavista.binaa.center.dto.response.AppointmentResponseDTO;
import com.novavista.binaa.center.dto.lookup.CaseLookupDTO;
import com.novavista.binaa.center.dto.lookup.StaffLookupDTO;
import com.novavista.binaa.center.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    /**
     * Creates a new appointment
     * @param appointmentDTO the appointment data
     * @return the created appointment with full details
     */
    AppointmentResponseDTO createAppointment(AppointmentDTO appointmentDTO);

    /**
     * Retrieves all appointments with case and staff details
     * @return list of appointments with full details
     */
    List<AppointmentResponseDTO> getAllAppointments();

    /**
     * Retrieves a specific appointment by ID with full details
     * @param id the appointment ID
     * @return the appointment with case and staff details
     */
    AppointmentResponseDTO getAppointmentById(Long id);

    /**
     * Retrieves appointments for a specific date/time
     * @param dateTime the date and time to search for
     * @return list of appointments with full details
     */
    List<AppointmentResponseDTO> getAppointmentsByDateTime(LocalDateTime dateTime);

    /**
     * Retrieves appointments by status
     * @param status the appointment status
     * @return list of appointments with full details
     */
    List<AppointmentResponseDTO> getAppointmentsByStatus(AppointmentStatus status);

    /**
     * Updates an existing appointment
     * @param id the appointment ID
     * @param appointmentDTO the updated appointment data
     * @return the updated appointment with full details
     */
    AppointmentResponseDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);

    /**
     * Deletes an appointment
     * @param id the appointment ID
     */
    void deleteAppointment(Long id);
}