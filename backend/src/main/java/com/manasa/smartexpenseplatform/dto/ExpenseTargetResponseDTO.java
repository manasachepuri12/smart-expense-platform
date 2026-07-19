package com.manasa.smartexpenseplatform.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTargetResponseDTO {

    private Long id;
    private String name;
}