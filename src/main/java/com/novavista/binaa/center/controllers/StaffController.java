package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.StaffDTO;
import com.novavista.binaa.center.services.StaffService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/staff")
@Slf4j
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) {
        log.info("Creating new staff member");
        return new ResponseEntity<>(staffService.createStaff(staffDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Long id) {
        log.info("Fetching staff member with id: {}", id);
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        log.info("Fetching all staff members");
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<StaffDTO>> getStaffByRole(@PathVariable String role) {
        log.info("Fetching staff members with role: {}", role);
        return ResponseEntity.ok(staffService.getStaffByRole(role));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffDTO staffDTO) {
        log.info("Updating staff member with id: {}", id);
        return ResponseEntity.ok(staffService.updateStaff(id, staffDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.info("Deleting staff member with id: {}", id);
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
