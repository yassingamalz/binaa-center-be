package com.novavista.binaa.center.dto;

import com.novavista.binaa.center.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long documentId;
    private Long caseId;
    private DocumentType type;
    private String filePath;
    private LocalDate uploadDate;
    private Long uploadedBy;
}