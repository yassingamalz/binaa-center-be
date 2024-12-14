package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.RewardDTO;
import com.novavista.binaa.center.enums.RewardStatus;
import com.novavista.binaa.center.services.RewardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@Slf4j
public class RewardController {
    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RewardDTO> createReward(@Valid @RequestBody RewardDTO rewardDTO) {
        log.info("Creating new reward");
        return new ResponseEntity<>(rewardService.createReward(rewardDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RewardDTO> getRewardById(@PathVariable Long id) {
        log.info("Fetching reward with id: {}", id);
        return ResponseEntity.ok(rewardService.getRewardById(id));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<RewardDTO>> getRewardsByStatus(@PathVariable RewardStatus status) {
        log.info("Fetching rewards with status: {}", status);
        return ResponseEntity.ok(rewardService.getRewardsByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RewardDTO> updateReward(@PathVariable Long id, @Valid @RequestBody RewardDTO rewardDTO) {
        log.info("Updating reward with id: {}", id);
        return ResponseEntity.ok(rewardService.updateReward(id, rewardDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        log.info("Deleting reward with id: {}", id);
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }
}
