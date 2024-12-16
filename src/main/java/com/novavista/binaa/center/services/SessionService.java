package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.SessionDTO;
import com.novavista.binaa.center.dto.response.SessionResponseDTO;
import com.novavista.binaa.center.enums.AttendanceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SessionService {
    /**
     * Creates a new session
     * @param sessionDTO the session data
     * @return the created session with case and staff details
     */
    SessionResponseDTO createSession(SessionDTO sessionDTO);

    /**
     * Retrieves all sessions with case and staff details
     * @return list of sessions with full details
     */
    List<SessionResponseDTO> getAllSessions();

    /**
     * Retrieves a specific session by ID with full details
     * @param id the session ID
     * @return the session with case and staff details
     */
    SessionResponseDTO getSessionById(Long id);

    /**
     * Retrieves sessions for a specific case
     * @param caseId the case ID
     * @return list of sessions with full details
     */
    List<SessionResponseDTO> getSessionsByCase(Long caseId);

    /**
     * Retrieves sessions within a date range
     * @param start start date/time
     * @param end end date/time
     * @return list of sessions with full details
     */
    List<SessionResponseDTO> getSessionsByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Updates an existing session
     * @param id the session ID
     * @param sessionDTO the updated session data
     * @return the updated session with full details
     */
    SessionResponseDTO updateSession(Long id, SessionDTO sessionDTO);

    /**
     * Deletes a session
     * @param id the session ID
     */
    void deleteSession(Long id);

    /**
     * Retrieves sessions by attendance status
     * @param status the attendance status
     * @return list of sessions with full details
     */
    List<SessionResponseDTO> getSessionsByAttendanceStatus(AttendanceStatus status);

    /**
     * Retrieves sessions for a specific staff member
     * @param staffId the staff ID
     * @return list of sessions with full details
     */
    List<SessionResponseDTO> getSessionsByStaff(Long staffId);

    /**
     * Retrieves session statistics for a given date range
     * @param start start date/time
     * @param end end date/time
     * @return map of session statistics
     */
    Map<String, Object> getSessionStats(LocalDateTime start, LocalDateTime end);
}