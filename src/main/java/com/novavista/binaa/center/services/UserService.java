package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    void changePassword(Long id, String oldPassword, String newPassword);
}
