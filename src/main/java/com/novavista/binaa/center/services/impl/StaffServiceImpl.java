package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.StaffDTO;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.StaffService;
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
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository, ModelMapper modelMapper) {
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        log.info("Creating new staff member: {}", staffDTO.getName());
        validateNewStaff(staffDTO);

        Staff staff = modelMapper.map(staffDTO, Staff.class);
        Staff savedStaff = staffRepository.save(staff);
        log.info("Staff created with ID: {}", savedStaff.getStaffId());
        return modelMapper.map(savedStaff, StaffDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDTO getStaffById(Long id) {
        return staffRepository.findById(id)
                .map(staff -> modelMapper.map(staff, StaffDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(staff -> modelMapper.map(staff, StaffDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffDTO> getStaffByRole(String role) {
        return staffRepository.findByRole(role).stream()
                .map(staff -> modelMapper.map(staff, StaffDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        modelMapper.map(staffDTO, existingStaff);
        existingStaff.setStaffId(id);

        Staff updatedStaff = staffRepository.save(existingStaff);
        log.info("Staff updated with ID: {}", id);
        return modelMapper.map(updatedStaff, StaffDTO.class);
    }

    @Override
    public void deleteStaff(Long id) {
        try {
            staffRepository.deleteById(id);
            log.info("Staff deleted with ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot delete staff: {}", e.getMessage());
            throw new RuntimeException("Cannot delete staff member due to existing references");
        }
    }

    private void validateNewStaff(StaffDTO staffDTO) {
        if (staffDTO.getName() == null || staffDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Staff name cannot be empty");
        }
    }
}
