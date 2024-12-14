package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.lookup.StaffLookupDTO;
import com.novavista.binaa.center.dto.request.StaffDTO;

import java.util.List;


public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    StaffDTO getStaffById(Long id);
    List<StaffDTO> getAllStaff();
    List<StaffDTO> getStaffByRole(String role);
    StaffDTO updateStaff(Long id, StaffDTO staffDTO);
    void deleteStaff(Long id);
    /**
     * Retrieves available staff for dropdown selection
     * @return list of staff with basic info
     */
    List<StaffLookupDTO> getAvailableStaffForLookup();
}
