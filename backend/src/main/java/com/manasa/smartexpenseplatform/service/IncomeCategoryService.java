package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;

public interface IncomeCategoryService {

    IncomeCategoryResponseDTO createCategory(
            IncomeCategoryRequestDTO request);

    List<IncomeCategoryResponseDTO> getAllCategories();

    IncomeCategoryResponseDTO getCategoryById(Long id);

    IncomeCategoryResponseDTO updateCategory(
            Long id,
            IncomeCategoryRequestDTO request);

    void deleteCategory(Long id);
}