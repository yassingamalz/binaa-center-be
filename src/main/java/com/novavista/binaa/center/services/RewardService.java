package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.RewardDTO;
import com.novavista.binaa.center.enums.RewardStatus;

import java.util.List;

public interface RewardService {
    RewardDTO createReward(RewardDTO rewardDTO);
    RewardDTO getRewardById(Long id);
    List<RewardDTO> getRewardsByStatus(RewardStatus status);
    RewardDTO updateReward(Long id, RewardDTO rewardDTO);
    void deleteReward(Long id);
}