package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.DocumentDTO;
import com.novavista.binaa.center.enums.DocumentType;
import com.novavista.binaa.center.services.DocumentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Slf4j
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DocumentDTO> createDocument(@Valid @RequestBody DocumentDTO documentDTO) {
        log.info("Creating new document");
        return new ResponseEntity<>(documentService.createDocument(documentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        log.info("Fetching document with id: {}", id);
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByCase(@PathVariable Long caseId) {
        log.info("Fetching documents for case: {}", caseId);
        return ResponseEntity.ok(documentService.getDocumentsByCase(caseId));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByType(@PathVariable DocumentType type) {
        log.info("Fetching documents of type: {}", type);
        return ResponseEntity.ok(documentService.getDocumentsByType(type));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        log.info("Fetching all documents");
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDTO documentDTO) {
        log.info("Updating document with id: {}", id);
        return ResponseEntity.ok(documentService.updateDocument(id, documentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        log.info("Deleting document with id: {}", id);
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}


