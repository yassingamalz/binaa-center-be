package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.AppointmentStatus;
import com.novavista.binaa.center.enums.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private Long caseId;
    private Long staffId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
    private AppointmentType type;
    private String notes;
}