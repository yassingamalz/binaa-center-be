package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.AppointmentDTO;
import com.novavista.binaa.center.entity.Appointment;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.AppointmentStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.AppointmentRepository;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  CaseRepository caseRepository,
                                  StaffRepository staffRepository,
                                  ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll().stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating new appointment for case ID: {}", appointmentDTO.getCaseId());
        validateAppointment(appointmentDTO);

        Case caseEntity = caseRepository.findById(appointmentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff staff = staffRepository.findById(appointmentDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        appointment.setCaseInfo(caseEntity);
        appointment.setStaff(staff);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Created appointment with ID: {}", savedAppointment.getAppointmentId());
        return modelMapper.map(savedAppointment, AppointmentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDateTime(LocalDateTime dateTime) {
        return appointmentRepository.findByDateTime(dateTime).stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment ID: {}", id);
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        validateAppointment(appointmentDTO);
        modelMapper.map(appointmentDTO, existingAppointment);
        existingAppointment.setAppointmentId(id);

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Updated appointment ID: {}", id);
        return modelMapper.map(updatedAppointment, AppointmentDTO.class);
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
