package com.novavista.binaa.center.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}