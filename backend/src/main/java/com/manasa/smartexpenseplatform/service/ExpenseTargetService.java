package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.ExpenseTargetRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseTargetResponseDTO;

public interface ExpenseTargetService {

    ExpenseTargetResponseDTO createTarget(ExpenseTargetRequestDTO request);

    List<ExpenseTargetResponseDTO> getAllTargets();

    ExpenseTargetResponseDTO getTargetById(Long id);

    ExpenseTargetResponseDTO updateTarget(Long id,
                                          ExpenseTargetRequestDTO request);

    void deleteTarget(Long id);
}
