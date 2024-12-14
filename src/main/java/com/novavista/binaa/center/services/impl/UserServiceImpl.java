package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.UserDTO;
import com.novavista.binaa.center.entity.User;
import com.novavista.binaa.center.exceptions.InvalidPasswordException;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.UserRepository;
import com.novavista.binaa.center.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user with username: {}", userDTO.getUsername());
        validateNewUser(userDTO);

        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            User savedUser = userRepository.save(user);
            log.info("Successfully created user with ID: {}", savedUser.getUserId());
            return modelMapper.map(savedUser, UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to create user: {}", e.getMessage());
            throw new RuntimeException("Failed to create user. Please try again.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new ResourceNotFoundException("User not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUpdateUser(userDTO, existingUser);

        // Don't update password through this method
        modelMapper.map(userDTO, existingUser);
        existingUser.setUserId(id); // Ensure ID doesn't change

        try {
            User updatedUser = userRepository.save(existingUser);
            log.info("Successfully updated user with ID: {}", id);
            return modelMapper.map(updatedUser, UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update user: {}", e.getMessage());
            throw new RuntimeException("Failed to update user. Please try again.");
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException("User not found");
            }
            userRepository.deleteById(id);
            log.info("Successfully deleted user with ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete user: {}", e.getMessage());
            throw new RuntimeException("Cannot delete user due to existing references");
        }
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("Invalid old password for user ID: {}", id);
            throw new InvalidPasswordException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Successfully changed password for user with ID: {}", id);
    }

    private void validateNewUser(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ValidationException("Username already exists");
        }
    }

    private void validateUpdateUser(UserDTO userDTO, User existingUser) {
        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ValidationException("Username already exists");
        }
    }
}