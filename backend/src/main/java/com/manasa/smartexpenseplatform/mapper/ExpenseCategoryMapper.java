package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;

public class ExpenseCategoryMapper {

    public static ExpenseCategory toEntity(
            ExpenseCategoryRequestDTO request,
            User user) {

        return ExpenseCategory.builder()
                .name(request.getName())
                .user(user)
                .build();
    }

    public static ExpenseCategoryResponseDTO toResponseDTO(
            ExpenseCategory category) {

        return ExpenseCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}