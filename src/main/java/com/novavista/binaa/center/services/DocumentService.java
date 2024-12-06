package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.DocumentDTO;
import com.novavista.binaa.center.enums.DocumentType;

import java.util.List;

public interface DocumentService {
    DocumentDTO createDocument(DocumentDTO documentDTO);
    DocumentDTO getDocumentById(Long id);
    List<DocumentDTO> getDocumentsByCase(Long caseId);
    List<DocumentDTO> getDocumentsByType(DocumentType type);
    DocumentDTO updateDocument(Long id, DocumentDTO documentDTO);
    void deleteDocument(Long id);
}
