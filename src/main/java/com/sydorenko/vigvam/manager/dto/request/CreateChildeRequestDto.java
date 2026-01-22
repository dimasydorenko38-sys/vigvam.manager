package com.sydorenko.vigvam.manager.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChildeRequestDto {
    private String Name;
    private String lastName;
    private String secondName;
    private LocalDate birthdayDate;
    private String interests;
    private String requestForLessons;
    private String diagnosis;
}
