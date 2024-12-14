package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.AppointmentDTO;
import com.novavista.binaa.center.enums.AppointmentStatus;
import com.novavista.binaa.center.enums.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO extends AppointmentDTO {
    private String caseName;
    private String staffName;

    public AppointmentResponseDTO(
            Long appointmentId,
            Long caseId,
            Long staffId,
            LocalDateTime dateTime,
            AppointmentStatus status,
            AppointmentType type,
            String notes,
            String caseName,
            String staffName
    ) {
        super(appointmentId, caseId, staffId, dateTime, status, type, notes);
        this.caseName = caseName;
        this.staffName = staffName;
    }
}
