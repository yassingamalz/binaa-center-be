package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.dto.SessionDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.CaseStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.CaseService;
import com.novavista.binaa.center.services.SessionService;
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
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final CaseRepository caseRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              CaseRepository caseRepository,
                              StaffRepository staffRepository,
                              ModelMapper modelMapper) {
        this.sessionRepository = sessionRepository;
        this.caseRepository = caseRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SessionDTO createSession(SessionDTO sessionDTO) {
        log.info("Creating session for case ID: {}", sessionDTO.getCaseId());
        validateSession(sessionDTO);

        Case caseEntity = caseRepository.findById(sessionDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        Staff staff = staffRepository.findById(sessionDTO.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Session session = modelMapper.map(sessionDTO, Session.class);
        session.setCaseInfo(caseEntity);
        session.setStaff(staff);

        Session savedSession = sessionRepository.save(session);
        return modelMapper.map(savedSession, SessionDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDTO getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDTO> getSessionsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return sessionRepository.findByCaseInfo(caseEntity).stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionDTO> getSessionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return sessionRepository.findBySessionDateBetween(start, end).stream()
                .map(session -> modelMapper.map(session, SessionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SessionDTO updateSession(Long id, SessionDTO sessionDTO) {
        Session existingSession = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        validateSession(sessionDTO);
        modelMapper.map(sessionDTO, existingSession);
        existingSession.setSessionId(id);
        return modelMapper.map(sessionRepository.save(existingSession), SessionDTO.class);
    }

    @Override
    public void deleteSession(Long id) {
        try {
            sessionRepository.deleteById(id);
            log.info("Deleted session ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete session");
        }
    }

    private void validateSession(SessionDTO sessionDTO) {
        if (sessionDTO.getPurpose() == null || sessionDTO.getPurpose().trim().isEmpty()) {
            throw new ValidationException("Session purpose is required");
        }
        if (sessionDTO.getSessionDate() == null) {
            throw new ValidationException("Session date is required");
        }
        if (sessionDTO.getDuration() == null || sessionDTO.getDuration() <= 0) {
            throw new ValidationException("Valid session duration is required");
        }
    }
}