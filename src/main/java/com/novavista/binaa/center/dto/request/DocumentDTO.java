package com.novavista.binaa.center.dto.request;

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
    private String fileName;
    private String contentType;
    private byte[] fileData;
    private Long fileSize;
    private LocalDate uploadDate;
    private Long uploadedBy;
}