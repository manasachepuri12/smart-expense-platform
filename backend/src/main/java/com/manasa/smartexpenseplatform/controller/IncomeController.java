package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.IncomeRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeResponseDTO;
import com.manasa.smartexpenseplatform.service.IncomeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public IncomeResponseDTO createIncome(
            @Valid @RequestBody IncomeRequestDTO request) {

        return incomeService.createIncome(request);
    }

    @GetMapping
    public List<IncomeResponseDTO> getAllIncome() {
        return incomeService.getAllIncome();
    }

    @GetMapping("/{id}")
    public IncomeResponseDTO getIncomeById(@PathVariable Long id) {
        return incomeService.getIncomeById(id);
    }

    @PutMapping("/{id}")
    public IncomeResponseDTO updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequestDTO request) {

        return incomeService.updateIncome(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}