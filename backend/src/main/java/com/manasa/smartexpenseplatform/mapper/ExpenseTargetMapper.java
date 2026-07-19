package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.ExpenseTargetRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseTargetResponseDTO;
import com.manasa.smartexpenseplatform.entity.ExpenseTarget;

public class ExpenseTargetMapper {

    public static ExpenseTarget toEntity(
            ExpenseTargetRequestDTO request) {

        return ExpenseTarget.builder()
                .name(request.getName())
                .build();
    }

    public static ExpenseTargetResponseDTO toResponseDTO(
            ExpenseTarget target) {

        return ExpenseTargetResponseDTO.builder()
                .id(target.getId())
                .name(target.getName())
                .build();
    }
}