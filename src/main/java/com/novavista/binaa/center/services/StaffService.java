package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.StaffDTO;

import java.util.List;


public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    StaffDTO getStaffById(Long id);
    List<StaffDTO> getAllStaff();
    List<StaffDTO> getStaffByRole(String role);
    StaffDTO updateStaff(Long id, StaffDTO staffDTO);
    void deleteStaff(Long id);
}