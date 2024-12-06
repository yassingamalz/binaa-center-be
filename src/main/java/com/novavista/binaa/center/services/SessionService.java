package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.SessionDTO;
import com.novavista.binaa.center.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {
    SessionDTO createSession(SessionDTO sessionDTO);
    SessionDTO getSessionById(Long id);
    List<SessionDTO> getSessionsByCase(Long caseId);
    List<SessionDTO> getSessionsByDateRange(LocalDateTime start, LocalDateTime end);
    SessionDTO updateSession(Long id, SessionDTO sessionDTO);
    void deleteSession(Long id);
}