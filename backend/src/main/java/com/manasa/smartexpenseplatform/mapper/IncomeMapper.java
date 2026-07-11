package com.manasa.smartexpenseplatform.mapper;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.dto.IncomeRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeResponseDTO;
import com.manasa.smartexpenseplatform.entity.Income;
import com.manasa.smartexpenseplatform.entity.IncomeCategory;

public class IncomeMapper {
    public static Income toEntity(
        IncomeRequestDTO request,
        User user,
        IncomeCategory category) {

    return Income.builder()
            .user(user)
            .incomeCategory(category)
            .amount(request.getAmount())
            .sourceName(request.getSourceName())
            .description(request.getDescription())
            .incomeDate(request.getIncomeDate())
            .build();
    }
    public static IncomeResponseDTO toResponseDTO(
        Income income) {

    return IncomeResponseDTO.builder()
            .id(income.getId())
            .amount(income.getAmount())
            .sourceName(income.getSourceName())
            .description(income.getDescription())
            .incomeDate(income.getIncomeDate())
            .createdAt(income.getCreatedAt())
            .incomeCategory(income.getIncomeCategory().getName())
            .build();
    }
}
