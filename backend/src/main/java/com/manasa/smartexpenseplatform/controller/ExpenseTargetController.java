package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.ExpenseTargetRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseTargetResponseDTO;
import com.manasa.smartexpenseplatform.service.ExpenseTargetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense-targets")
public class ExpenseTargetController {

    private final ExpenseTargetService expenseTargetService;

    public ExpenseTargetController(
            ExpenseTargetService expenseTargetService) {

        this.expenseTargetService = expenseTargetService;
    }

    @PostMapping
    public ResponseEntity<ExpenseTargetResponseDTO> createTarget(
            @Valid @RequestBody ExpenseTargetRequestDTO request) {

        return new ResponseEntity<>(
                expenseTargetService.createTarget(request),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseTargetResponseDTO>> getAllTargets() {

        return ResponseEntity.ok(
                expenseTargetService.getAllTargets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTargetResponseDTO> getTargetById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseTargetService.getTargetById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseTargetResponseDTO> updateTarget(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseTargetRequestDTO request) {

        return ResponseEntity.ok(
                expenseTargetService.updateTarget(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarget(
            @PathVariable Long id) {

        expenseTargetService.deleteTarget(id);

        return ResponseEntity.ok(
                "Expense Target deleted successfully.");
    }
}
