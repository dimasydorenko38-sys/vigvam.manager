package com.sydorenko.vigvam.manager.dto.request.users;

import lombok.*;

import java.time.LocalDateTime;

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
