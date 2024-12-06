package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String filePath;
    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;
}
