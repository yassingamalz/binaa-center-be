package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.AppointmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO extends AppointmentDTO {
    private String caseName;
    private String staffName;
}