package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.lookup.CaseLookupDTO;
import com.novavista.binaa.center.dto.lookup.StaffLookupDTO;
import com.novavista.binaa.center.dto.request.AppointmentDTO;
import com.novavista.binaa.center.dto.response.AppointmentResponseDTO;
import com.novavista.binaa.center.entity.Appointment;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.AppointmentStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.mapper.AppointmentMapper;
import com.novavista.binaa.center.repository.AppointmentRepository;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  CaseRepository caseRepository,
                                  StaffRepository staffRepository,
                                  AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAllAppointments() {
        log.info("Fetching all appointments with details");
        return appointmentRepository.findAllWithDetails();
    }

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating new appointment for case ID: {}", appointmentDTO.getCaseId());
        validateAppointment(appointmentDTO);
        validateAvailability(appointmentDTO);

        Case caseEntity = caseRepository.findById(appointmentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff staff = staffRepository.findById(appointmentDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setCaseInfo(caseEntity);
        appointment.setStaff(staff);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Created appointment with ID: {}", savedAppointment.getAppointmentId());

        return appointmentRepository.findByIdWithDetails(savedAppointment.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Created appointment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDateTime(LocalDateTime dateTime) {
        log.info("Fetching appointments for date: {}", dateTime);
        return appointmentRepository.findByDateTimeWithDetails(dateTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByStatus(AppointmentStatus status) {
        log.info("Fetching appointments with status: {}", status);
        return appointmentRepository.findByStatusWithDetails(status);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment ID: {}", id);

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        validateAppointment(appointmentDTO);
        validateAvailability(appointmentDTO, id);

        // Update case and staff if changed
        if (!existingAppointment.getCaseInfo().getCaseId().equals(appointmentDTO.getCaseId())) {
            Case newCase = caseRepository.findById(appointmentDTO.getCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
            existingAppointment.setCaseInfo(newCase);
        }

        if (!existingAppointment.getStaff().getStaffId().equals(appointmentDTO.getStaffId())) {
            Staff newStaff = staffRepository.findById(appointmentDTO.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            existingAppointment.setStaff(newStaff);
        }

        // Update other fields
        existingAppointment.setDateTime(appointmentDTO.getDateTime());
        existingAppointment.setStatus(appointmentDTO.getStatus());
        existingAppointment.setType(appointmentDTO.getType());
        existingAppointment.setNotes(appointmentDTO.getNotes());

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Updated appointment ID: {}", id);

        return appointmentRepository.findByIdWithDetails(updatedAppointment.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Updated appointment not found"));
    }

    @Override
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment ID: {}", id);
        try {
            appointmentRepository.deleteById(id);
            log.info("Deleted appointment ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete appointment: {}", e.getMessage());
            throw new ValidationException("Cannot delete appointment due to existing references");
        }
    }

    private void validateAppointment(AppointmentDTO appointmentDTO) {
        log.debug("Validating appointment data");

        if (appointmentDTO.getDateTime() == null) {
            throw new ValidationException("Appointment date/time is required");
        }
        if (appointmentDTO.getDateTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment cannot be scheduled in the past");
        }
        if (appointmentDTO.getCaseId() == null) {
            throw new ValidationException("Case ID is required");
        }
        if (appointmentDTO.getStaffId() == null) {
            throw new ValidationException("Staff ID is required");
        }
        if (appointmentDTO.getType() == null) {
            throw new ValidationException("Appointment type is required");
        }
    }

    private void validateAvailability(AppointmentDTO appointmentDTO) {
        validateAvailability(appointmentDTO, null);
    }

    private void validateAvailability(AppointmentDTO appointmentDTO, Long excludeAppointmentId) {
        LocalDateTime startTime = appointmentDTO.getDateTime();
        LocalDateTime endTime = startTime.plusMinutes(30); // Assuming 30-minute appointments

        // Check staff availability
        boolean staffBusy = appointmentRepository.isStaffBusy(
                appointmentDTO.getStaffId(),
                startTime,
                endTime,
                excludeAppointmentId
        );
        if (staffBusy) {
            throw new ValidationException("Staff is not available at the selected time");
        }

        // Check case availability
        boolean caseBusy = appointmentRepository.isCaseBusy(
                appointmentDTO.getCaseId(),
                startTime,
                endTime,
                excludeAppointmentId
        );
        if (caseBusy) {
            throw new ValidationException("Case already has an appointment at the selected time");
        }
    }
}