package com.sydorenko.vigvam.manager.dto.request.users.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSalaryEmployeeRequestDto {
    @NonNull Long serviceTypeId;
    @NonNull Long value;
    Long premiumValue;
    @NotBlank
    String lessonType;
}
