package com.sydorenko.vigvam.manager.dto.request.users.child;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChildRequestDto {
    private Long clientId;

    @NotBlank(message = "Ім'я дитини не може бути порожнім")
    private String name;
    @NotBlank(message = "Прізвище дитини не може бути порожнім")
    private String lastName;
    private String secondName;
    @NonNull
    private LocalDate birthdayDate;
    private String interests;
    private String requestForLessons;
    private String diagnosis;
}
