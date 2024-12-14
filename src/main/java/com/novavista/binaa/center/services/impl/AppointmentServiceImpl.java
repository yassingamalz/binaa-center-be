package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.AppointmentDTO;
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
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Fetching all appointments");
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointmentMapper.toDtoList(appointments);
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating new appointment for case ID: {}", appointmentDTO.getCaseId());
        validateAppointment(appointmentDTO);

        Case caseEntity = caseRepository.findById(appointmentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff staff = staffRepository.findById(appointmentDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setCaseInfo(caseEntity);
        appointment.setStaff(staff);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Created appointment with ID: {}", savedAppointment.getAppointmentId());
        return appointmentMapper.toDto(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return appointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDateTime(LocalDateTime dateTime) {
        List<Appointment> appointments = appointmentRepository.findByDateTime(dateTime);
        return appointmentMapper.toDtoList(appointments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByStatus(AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        return appointmentMapper.toDtoList(appointments);
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment ID: {}", id);
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        validateAppointment(appointmentDTO);
        appointmentMapper.toEntity(appointmentDTO);
        existingAppointment.setAppointmentId(id);

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Updated appointment ID: {}", id);
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment ID: {}", id);
        try {
            appointmentRepository.deleteById(id);
            log.info("Deleted appointment ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete appointment: {}", e.getMessage());
            throw new RuntimeException("Cannot delete appointment");
        }
    }

    private void validateAppointment(AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getDateTime() == null) {
            throw new ValidationException("Appointment date/time is required");
        }
        if (appointmentDTO.getDateTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment cannot be scheduled in the past");
        }
    }
}