package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.RewardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    private Long rewardId;
    private String name;
    private Integer pointsRequired;
    private String description;
    private LocalDate validUntil;
    private RewardStatus status;
}
