// src/main/java/com/novavista/binaa/center/dto/response/SessionResponseDTO.java
package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.SessionDTO;
import com.novavista.binaa.center.enums.AttendanceStatus;
import com.novavista.binaa.center.enums.SessionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDTO extends SessionDTO {
    private String caseName;
    private String staffName;

    public SessionResponseDTO(
            Long sessionId,
            Long caseId,
            String purpose,
            LocalDateTime sessionDate,
            String notes,
            Long staffId,
            SessionType sessionType,
            AttendanceStatus attendanceStatus,
            Integer duration,
            String caseName,
            String staffName
    ) {
        super(sessionId, caseId, purpose, sessionDate, notes, staffId, 
              sessionType, attendanceStatus, duration);
        this.caseName = caseName;
        this.staffName = staffName;
    }
}