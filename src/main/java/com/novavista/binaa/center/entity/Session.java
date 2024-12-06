package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.AttendanceStatus;
import com.novavista.binaa.center.enums.SessionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    private String purpose;
    private LocalDateTime sessionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    private Integer duration;

    @Column(columnDefinition = "TEXT")
    private String goalsAchieved;

    @Column(columnDefinition = "TEXT")
    private String nextSessionPlan;

    private String attachments;
}