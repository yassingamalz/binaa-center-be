package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.SessionDTO;
import com.novavista.binaa.center.services.SessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/sessions")
@Slf4j
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<SessionDTO> createSession(@Valid @RequestBody SessionDTO sessionDTO) {
        log.info("Creating new session");
        return new ResponseEntity<>(sessionService.createSession(sessionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        log.info("Fetching session with id: {}", id);
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<SessionDTO>> getSessionsByCase(@PathVariable Long caseId) {
        log.info("Fetching sessions for case: {}", caseId);
        return ResponseEntity.ok(sessionService.getSessionsByCase(caseId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        log.info("Fetching all sessions");
        return ResponseEntity.ok(sessionService.getAllSessions());
    }
    @GetMapping("/dateRange")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<SessionDTO>> getSessionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("Fetching sessions between {} and {}", start, end);
        return ResponseEntity.ok(sessionService.getSessionsByDateRange(start, end));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<SessionDTO> updateSession(@PathVariable Long id, @Valid @RequestBody SessionDTO sessionDTO) {
        log.info("Updating session with id: {}", id);
        return ResponseEntity.ok(sessionService.updateSession(id, sessionDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        log.info("Deleting session with id: {}", id);
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
