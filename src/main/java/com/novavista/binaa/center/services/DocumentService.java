package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.DocumentDTO;
import com.novavista.binaa.center.entity.Document;
import com.novavista.binaa.center.enums.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentDTO createDocument(DocumentDTO documentDTO);
    DocumentDTO getDocumentById(Long id);
    List<DocumentDTO> getDocumentsByCase(Long caseId);
    List<DocumentDTO> getDocumentsByType(DocumentType type);
    List<DocumentDTO> getAllDocuments();
    DocumentDTO uploadDocument(MultipartFile file, DocumentType type, Long caseId);
    Document getDocumentForDownload(Long id);
    DocumentDTO updateDocument(Long id, DocumentDTO documentDTO);
    void deleteDocument(Long id);
}
