package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequestDto {
    @NonNull private Long employeeId;
    @NonNull private String name;
    @NonNull private String lastName;
    @NonNull private String phone;
    @NonNull private LocalDate birthday;
}
