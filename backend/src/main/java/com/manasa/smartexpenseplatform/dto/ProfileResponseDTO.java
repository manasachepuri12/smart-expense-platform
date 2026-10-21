
package com.manasa.smartexpenseplatform.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDTO {

    private Long id;
    private String name;
    private String email;
}
