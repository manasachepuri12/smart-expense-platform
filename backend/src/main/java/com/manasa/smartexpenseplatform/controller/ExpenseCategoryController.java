package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;
import com.manasa.smartexpenseplatform.service.ExpenseCategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense-categories")
@Validated
@Tag(
    name = "Expense Categories",
    description = "Manage expense categories"
)
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @PostMapping
    public ResponseEntity<ExpenseCategoryResponseDTO> createCategory(
            @Valid @RequestBody ExpenseCategoryRequestDTO request) {

        ExpenseCategoryResponseDTO response =
                expenseCategoryService.createCategory(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseCategoryResponseDTO>> getAllCategories() {

        return ResponseEntity.ok(
                expenseCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseCategoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseCategoryRequestDTO request) {

        return ResponseEntity.ok(
                expenseCategoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        expenseCategoryService.deleteCategory(id);

        return ResponseEntity.ok("Expense Category deleted successfully.");
    }
}
