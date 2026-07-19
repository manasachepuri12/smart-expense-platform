package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;
import com.manasa.smartexpenseplatform.service.IncomeCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/income-categories")
@Validated
@Tag(
        name = "Income Categories",
        description = "APIs for managing income categories"
)
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;

    public IncomeCategoryController(IncomeCategoryService incomeCategoryService) {
        this.incomeCategoryService = incomeCategoryService;
    }

    @Operation(
            summary = "Create a new income category",
            description = "Creates a new income category for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Income category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<IncomeCategoryResponseDTO> createCategory(
            @Valid @RequestBody IncomeCategoryRequestDTO request) {

        IncomeCategoryResponseDTO response =
                incomeCategoryService.createCategory(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all income categories",
            description = "Retrieves all income categories of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<IncomeCategoryResponseDTO>> getAllCategories() {

        return ResponseEntity.ok(
                incomeCategoryService.getAllCategories());
    }

    @Operation(
            summary = "Get income category by ID",
            description = "Retrieves a specific income category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income category retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<IncomeCategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                incomeCategoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Update income category",
            description = "Updates an existing income category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<IncomeCategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody IncomeCategoryRequestDTO request) {

        return ResponseEntity.ok(
                incomeCategoryService.updateCategory(id, request));
    }

    @Operation(
            summary = "Delete income category",
            description = "Deletes an income category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income category deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        incomeCategoryService.deleteCategory(id);

        return ResponseEntity.ok("Income Category deleted successfully.");
    }
}