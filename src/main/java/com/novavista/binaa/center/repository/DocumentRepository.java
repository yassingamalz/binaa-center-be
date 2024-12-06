package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Document;
import com.novavista.binaa.center.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCaseInfo(Case caseInfo);
    List<Document> findByType(DocumentType type);
}