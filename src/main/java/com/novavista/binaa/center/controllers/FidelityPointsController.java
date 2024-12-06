package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.FidelityPointsDTO;
import com.novavista.binaa.center.enums.PointsStatus;
import com.novavista.binaa.center.services.FidelityPointsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fidelity-points")
@Slf4j
public class FidelityPointsController {
    private final FidelityPointsService fidelityPointsService;

    @Autowired
    public FidelityPointsController(FidelityPointsService fidelityPointsService) {
        this.fidelityPointsService = fidelityPointsService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<FidelityPointsDTO> createPoints(@Valid @RequestBody FidelityPointsDTO pointsDTO) {
        log.info("Creating new fidelity points transaction");
        return new ResponseEntity<>(fidelityPointsService.createPoints(pointsDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<FidelityPointsDTO> getPointsById(@PathVariable Long id) {
        log.info("Fetching fidelity points with id: {}", id);
        return ResponseEntity.ok(fidelityPointsService.getPointsById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<FidelityPointsDTO>> getPointsByCase(@PathVariable Long caseId) {
        log.info("Fetching fidelity points for case: {}", caseId);
        return ResponseEntity.ok(fidelityPointsService.getPointsByCase(caseId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<FidelityPointsDTO>> getPointsByStatus(@PathVariable PointsStatus status) {
        log.info("Fetching fidelity points with status: {}", status);
        return ResponseEntity.ok(fidelityPointsService.getPointsByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FidelityPointsDTO> updatePoints(@PathVariable Long id, @Valid @RequestBody FidelityPointsDTO pointsDTO) {
        log.info("Updating fidelity points with id: {}", id);
        return ResponseEntity.ok(fidelityPointsService.updatePoints(id, pointsDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePoints(@PathVariable Long id) {
        log.info("Deleting fidelity points with id: {}", id);
        fidelityPointsService.deletePoints(id);
        return ResponseEntity.noContent().build();
    }
}