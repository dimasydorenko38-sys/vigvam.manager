package com.sydorenko.vigvam.manager.dto.request.users;

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
    @NonNull
    private String name;
    @NonNull
    private String lastName;
    @NonNull
    private String secondName;
    @NonNull
    private LocalDate birthdayDate;
    private String interests;
    private String requestForLessons;
    private String diagnosis;
}
