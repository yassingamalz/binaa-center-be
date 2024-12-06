package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.RewardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "rewards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;

    private String name;
    private Integer pointsRequired;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate validUntil;

    @Enumerated(EnumType.STRING)
    private RewardStatus status;
}

