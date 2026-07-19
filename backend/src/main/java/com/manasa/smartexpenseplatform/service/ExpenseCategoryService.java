package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;

public interface ExpenseCategoryService {

    ExpenseCategoryResponseDTO createCategory(
            ExpenseCategoryRequestDTO request);

    List<ExpenseCategoryResponseDTO> getAllCategories();

    ExpenseCategoryResponseDTO getCategoryById(Long id);

    ExpenseCategoryResponseDTO updateCategory(
            Long id,
            ExpenseCategoryRequestDTO request);

    void deleteCategory(Long id);
}
