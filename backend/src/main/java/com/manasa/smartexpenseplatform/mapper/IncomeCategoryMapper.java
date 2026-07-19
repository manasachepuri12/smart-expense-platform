package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.IncomeCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeCategoryResponseDTO;
import com.manasa.smartexpenseplatform.entity.IncomeCategory;
import com.manasa.smartexpenseplatform.entity.User;

public class IncomeCategoryMapper {

    public static IncomeCategory toEntity(
            IncomeCategoryRequestDTO request,
            User user) {

        return IncomeCategory.builder()
                .name(request.getName())
                .description(null)
                .isDefault(false)
                .user(user)
                .build();
    }

    public static IncomeCategoryResponseDTO toResponseDTO(
            IncomeCategory category) {

        return IncomeCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}