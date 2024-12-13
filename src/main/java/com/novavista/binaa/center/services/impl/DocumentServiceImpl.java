package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.DocumentDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Document;
import com.novavista.binaa.center.entity.User;
import com.novavista.binaa.center.enums.DocumentType;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.DocumentRepository;
import com.novavista.binaa.center.repository.UserRepository;
import com.novavista.binaa.center.services.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               CaseRepository caseRepository,
                               UserRepository userRepository,
                               ModelMapper modelMapper) {
        this.documentRepository = documentRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DocumentDTO createDocument(DocumentDTO documentDTO) {
        log.info("Creating new document for case ID: {}", documentDTO.getCaseId());
        validateDocument(documentDTO);

        Case caseEntity = caseRepository.findById(documentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        User uploadedBy = userRepository.findById(documentDTO.getUploadedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Document document = modelMapper.map(documentDTO, Document.class);
        document.setCaseInfo(caseEntity);
        document.setUploadedBy(uploadedBy);
        document.setUploadDate(LocalDate.now());

        Document savedDocument = documentRepository.save(document);
        log.info("Created document with ID: {}", savedDocument.getDocumentId());
        return modelMapper.map(savedDocument, DocumentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentDTO getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return documentRepository.findByCaseInfo(caseEntity).stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByType(DocumentType type) {
        return documentRepository.findByType(type).stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(document -> modelMapper.map(document, DocumentDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public DocumentDTO updateDocument(Long id, DocumentDTO documentDTO) {
        log.info("Updating document ID: {}", id);
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        validateDocument(documentDTO);
        modelMapper.map(documentDTO, existingDocument);
        existingDocument.setDocumentId(id);

        Document updatedDocument = documentRepository.save(existingDocument);
        log.info("Updated document ID: {}", id);
        return modelMapper.map(updatedDocument, DocumentDTO.class);
    }

    @Override
    public void deleteDocument(Long id) {
        log.info("Deleting document ID: {}", id);
        try {
            documentRepository.deleteById(id);
            log.info("Deleted document ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete document: {}", e.getMessage());
            throw new RuntimeException("Cannot delete document");
        }
    }

    private void validateDocument(DocumentDTO documentDTO) {
        if (documentDTO.getType() == null) {
            throw new ValidationException("Document type is required");
        }
        if (documentDTO.getFilePath() == null || documentDTO.getFilePath().trim().isEmpty()) {
            throw new ValidationException("File path is required");
        }
    }
}
