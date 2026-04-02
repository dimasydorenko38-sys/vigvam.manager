package com.sydorenko.vigvam.manager.dto.request.users.child;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChildRequestDto {
    @NonNull
    private Long childId;
    @NotBlank(message = "Ім'я не може бути порожнім")
    private String name;
    @NotBlank(message = "Прізвище не може бути порожнім")
    private String lastName;
    private String secondName;
    @NonNull
    private LocalDate birthdayDate;
    private String interests;
    private String requestForLessons;
    private String diagnosis;
}
