package com.sydorenko.vigvam.manager.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSalaryEmployeeRequestDto {
    @NonNull Long serviceTypeId;
    @NonNull Long value;
    Long premiumValue;
    @NonNull String lessonType;
}
