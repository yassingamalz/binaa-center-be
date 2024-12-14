package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDTO user;
}