package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.AppointmentStatus;
import com.novavista.binaa.center.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
