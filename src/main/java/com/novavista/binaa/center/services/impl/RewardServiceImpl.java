package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.dto.RewardDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Reward;
import com.novavista.binaa.center.enums.CaseStatus;
import com.novavista.binaa.center.enums.RewardStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.RewardRepository;
import com.novavista.binaa.center.services.CaseService;
import com.novavista.binaa.center.services.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RewardServiceImpl(RewardRepository rewardRepository, ModelMapper modelMapper) {
        this.rewardRepository = rewardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RewardDTO createReward(RewardDTO rewardDTO) {
        log.info("Creating new reward: {}", rewardDTO.getName());
        validateReward(rewardDTO);
        Reward reward = modelMapper.map(rewardDTO, Reward.class);
        Reward savedReward = rewardRepository.save(reward);
        return modelMapper.map(savedReward, RewardDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RewardDTO getRewardById(Long id) {
        return rewardRepository.findById(id)
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found"));
    }

    @Override
    public List<RewardDTO> getRewardsByStatus(RewardStatus status) {
        return rewardRepository.findByStatus(status).stream()
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RewardDTO updateReward(Long id, RewardDTO rewardDTO) {
        Reward existingReward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found"));
        validateReward(rewardDTO);
        modelMapper.map(rewardDTO, existingReward);
        existingReward.setRewardId(id);
        return modelMapper.map(rewardRepository.save(existingReward), RewardDTO.class);
    }

    @Override
    public void deleteReward(Long id) {
        try {
            rewardRepository.deleteById(id);
            log.info("Deleted reward ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete reward");
        }
    }

    private void validateReward(RewardDTO rewardDTO) {
        if (rewardDTO.getName() == null || rewardDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Reward name is required");
        }
        if (rewardDTO.getPointsRequired() == null || rewardDTO.getPointsRequired() <= 0) {
            throw new ValidationException("Valid points amount required");
        }
    }
}
