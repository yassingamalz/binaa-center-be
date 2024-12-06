package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Reward;
import com.novavista.binaa.center.enums.RewardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByStatus(RewardStatus status);
}