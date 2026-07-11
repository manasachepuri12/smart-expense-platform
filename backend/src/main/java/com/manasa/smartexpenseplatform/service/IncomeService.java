package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.IncomeRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeResponseDTO;

public interface IncomeService {

    IncomeResponseDTO createIncome(IncomeRequestDTO request);

    List<IncomeResponseDTO> getAllIncome();

    IncomeResponseDTO getIncomeById(Long id);

    IncomeResponseDTO updateIncome(Long id, IncomeRequestDTO request);

    void deleteIncome(Long id);
}