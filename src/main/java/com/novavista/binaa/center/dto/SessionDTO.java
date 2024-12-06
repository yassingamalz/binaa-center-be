package com.novavista.binaa.center.dto;

import com.novavista.binaa.center.enums.AttendanceStatus;
import com.novavista.binaa.center.enums.SessionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private Long sessionId;
    private Long caseId;
    private String purpose;
    private LocalDateTime sessionDate;
    private String notes;
    private Long staffId;
    private SessionType sessionType;
    private AttendanceStatus attendanceStatus;
    private Integer duration;
}