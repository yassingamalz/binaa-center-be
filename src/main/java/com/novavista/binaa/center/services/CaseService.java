package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.CaseDTO;

import java.util.List;
public interface CaseService {
    CaseDTO createCase(CaseDTO caseDTO);
    CaseDTO getCaseById(Long id);
    List<CaseDTO> getAllActiveCases();
    List<CaseDTO> searchCasesByName(String name);
    CaseDTO updateCase(Long id, CaseDTO caseDTO);
    void deleteCase(Long id);
}