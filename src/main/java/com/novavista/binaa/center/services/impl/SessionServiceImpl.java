package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.SessionDTO;
import com.novavista.binaa.center.dto.response.SessionResponseDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.AttendanceStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.mapper.SessionMapper;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              CaseRepository caseRepository,
                              StaffRepository staffRepository,
                              SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getAllSessions() {
        log.info("Fetching all sessions");
        List<Session> sessions = sessionRepository.findAllWithDetails();
        return sessionMapper.toResponseDtoList(sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponseDTO getSessionById(Long id) {
        log.info("Fetching session with id: {}", id);
        Session session = sessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        return sessionMapper.toResponseDto(session);
    }

    @Override
    public SessionResponseDTO createSession(SessionDTO sessionDTO) {
        log.info("Creating new session for case ID: {}", sessionDTO.getCaseId());
        validateSession(sessionDTO);

        Case caseEntity = caseRepository.findById(sessionDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff staff = staffRepository.findById(sessionDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Session session = sessionMapper.toEntity(sessionDTO);
        session.setCaseInfo(caseEntity);
        session.setStaff(staff);

        Session savedSession = sessionRepository.save(session);
        log.info("Created session with ID: {}", savedSession.getSessionId());

        Session sessionWithDetails = sessionRepository.findByIdWithDetails(savedSession.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Created session not found"));
        return sessionMapper.toResponseDto(sessionWithDetails);
    }

    @Override
    public SessionResponseDTO updateSession(Long id, SessionDTO sessionDTO) {
        log.info("Updating session ID: {}", id);

        Session existingSession = sessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // Only validate fields that are being updated
        if (sessionDTO.getPurpose() != null || sessionDTO.getSessionDate() != null
                || sessionDTO.getDuration() != null || sessionDTO.getCaseId() != null
                || sessionDTO.getStaffId() != null || sessionDTO.getSessionType() != null) {
            validatePartialUpdate(sessionDTO, existingSession);
        }

        // Update case if provided and changed
        if (sessionDTO.getCaseId() != null &&
                !existingSession.getCaseInfo().getCaseId().equals(sessionDTO.getCaseId())) {
            Case newCase = caseRepository.findById(sessionDTO.getCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
            existingSession.setCaseInfo(newCase);
        }

        // Update staff if provided and changed
        if (sessionDTO.getStaffId() != null &&
                !existingSession.getStaff().getStaffId().equals(sessionDTO.getStaffId())) {
            Staff newStaff = staffRepository.findById(sessionDTO.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            existingSession.setStaff(newStaff);
        }

        // Update other fields only if they are provided
        if (sessionDTO.getPurpose() != null) {
            existingSession.setPurpose(sessionDTO.getPurpose());
        }
        if (sessionDTO.getSessionDate() != null) {
            existingSession.setSessionDate(sessionDTO.getSessionDate());
        }
        if (sessionDTO.getSessionType() != null) {
            existingSession.setSessionType(sessionDTO.getSessionType());
        }
        if (sessionDTO.getAttendanceStatus() != null) {
            existingSession.setAttendanceStatus(sessionDTO.getAttendanceStatus());
        }
        if (sessionDTO.getDuration() != null) {
            existingSession.setDuration(sessionDTO.getDuration());
        }
        if (sessionDTO.getNotes() != null) {
            existingSession.setNotes(sessionDTO.getNotes());
        }

        Session updatedSession = sessionRepository.save(existingSession);
        log.info("Updated session ID: {}", id);

        Session sessionWithDetails = sessionRepository.findByIdWithDetails(updatedSession.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Updated session not found"));
        return sessionMapper.toResponseDto(sessionWithDetails);
    }

    private void validatePartialUpdate(SessionDTO sessionDTO, Session existingSession) {
        if (sessionDTO.getPurpose() != null && sessionDTO.getPurpose().trim().isEmpty()) {
            throw new ValidationException("Session purpose cannot be empty");
        }
        if (sessionDTO.getSessionDate() != null && sessionDTO.getSessionDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Session cannot be scheduled in the past");
        }
        if (sessionDTO.getDuration() != null && sessionDTO.getDuration() <= 0) {
            throw new ValidationException("Valid session duration is required");
        }
        // Only validate IDs if they're being changed
        if (sessionDTO.getCaseId() != null && !sessionDTO.getCaseId().equals(existingSession.getCaseInfo().getCaseId())) {
            if (!caseRepository.existsById(sessionDTO.getCaseId())) {
                throw new ResourceNotFoundException("Case not found");
            }
        }
        if (sessionDTO.getStaffId() != null && !sessionDTO.getStaffId().equals(existingSession.getStaff().getStaffId())) {
            if (!staffRepository.existsById(sessionDTO.getStaffId())) {
                throw new ResourceNotFoundException("Staff not found");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessionsByCase(Long caseId) {
        log.info("Fetching sessions for case ID: {}", caseId);
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        List<Session> sessions = sessionRepository.findByCaseInfoWithDetails(caseEntity);
        return sessionMapper.toResponseDtoList(sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessionsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching sessions between {} and {}", start, end);
        List<Session> sessions = sessionRepository.findBySessionDateBetween(start, end);
        return sessionMapper.toResponseDtoList(sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessionsByAttendanceStatus(AttendanceStatus status) {
        log.info("Fetching sessions with status: {}", status);
        List<Session> sessions = sessionRepository.findByAttendanceStatus(status);
        return sessionMapper.toResponseDtoList(sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDTO> getSessionsByStaff(Long staffId) {
        log.info("Fetching sessions for staff ID: {}", staffId);
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        List<Session> sessions = sessionRepository.findByStaffWithDetails(staff);
        return sessionMapper.toResponseDtoList(sessions);
    }

    @Override
    public void deleteSession(Long id) {
        log.info("Deleting session ID: {}", id);
        try {
            sessionRepository.deleteById(id);
            log.info("Deleted session ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete session: {}", e.getMessage());
            throw new ValidationException("Cannot delete session due to existing references");
        }
    }

    public Map<String, Object> getSessionStats(LocalDateTime start, LocalDateTime end) {
        List<SessionResponseDTO> sessions = getSessionsByDateRange(start, end);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", sessions.size());
        stats.put("attendanceRate", calculateAttendanceRate(sessions));
        stats.put("cancelledSessions", countCancelledSessions(sessions));

        return stats;
    }

    private double calculateAttendanceRate(List<SessionResponseDTO> sessions) {
        if (sessions.isEmpty()) return 0.0;

        long presentSessions = sessions.stream()
                .filter(session -> session.getAttendanceStatus() == AttendanceStatus.PRESENT)
                .count();

        return (double) presentSessions / sessions.size() * 100.0;
    }

    private long countCancelledSessions(List<SessionResponseDTO> sessions) {
        return sessions.stream()
                .filter(session -> session.getAttendanceStatus() == AttendanceStatus.ABSENT)
                .count();
    }

    private void validateSession(SessionDTO sessionDTO) {
        log.debug("Validating session data");

        if (sessionDTO.getPurpose() == null || sessionDTO.getPurpose().trim().isEmpty()) {
            throw new ValidationException("Session purpose is required");
        }
        if (sessionDTO.getSessionDate() == null) {
            throw new ValidationException("Session date is required");
        }
        if (sessionDTO.getSessionDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Session cannot be scheduled in the past");
        }
        if (sessionDTO.getDuration() == null || sessionDTO.getDuration() <= 0) {
            throw new ValidationException("Valid session duration is required");
        }
        if (sessionDTO.getCaseId() == null) {
            throw new ValidationException("Case ID is required");
        }
        if (sessionDTO.getStaffId() == null) {
            throw new ValidationException("Staff ID is required");
        }
        if (sessionDTO.getSessionType() == null) {
            throw new ValidationException("Session type is required");
        }
    }
}