package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;
import com.manasa.smartexpenseplatform.service.IncomeCategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/income-categories")
@Validated
@Tag(
    name = "Income Categories",
    description = "Manage income categories"
)
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;

    public IncomeCategoryController(IncomeCategoryService incomeCategoryService) {
        this.incomeCategoryService = incomeCategoryService;
    }

    @PostMapping
    public ResponseEntity<IncomeCategoryResponseDTO> createCategory(
            @Valid @RequestBody IncomeCategoryRequestDTO request) {

        IncomeCategoryResponseDTO response =
                incomeCategoryService.createCategory(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeCategoryResponseDTO>> getAllCategories() {

        return ResponseEntity.ok(
                incomeCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeCategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                incomeCategoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeCategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody IncomeCategoryRequestDTO request) {

        return ResponseEntity.ok(
                incomeCategoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        incomeCategoryService.deleteCategory(id);

        return ResponseEntity.ok("Income Category deleted successfully.");
    }
}