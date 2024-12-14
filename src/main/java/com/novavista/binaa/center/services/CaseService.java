package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.lookup.CaseLookupDTO;
import com.novavista.binaa.center.dto.request.CaseDTO;

import java.util.List;
public interface CaseService {
    CaseDTO createCase(CaseDTO caseDTO);
    CaseDTO getCaseById(Long id);
    List<CaseDTO> getAllCases();
    List<CaseDTO> getCasesByStatus(String status);
    List<CaseDTO> getAllActiveCases();
    List<CaseDTO> searchCasesByName(String name);
    CaseDTO updateCase(Long id, CaseDTO caseDTO);
    void deleteCase(Long id);
    /**
     * Retrieves active cases for dropdown selection
     * @return list of cases with basic info
     */
    List<CaseLookupDTO> getActiveCasesForLookup();

}
