package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChildRequestDto {
    private ClientEntity client;
    private String name;
    private String lastName;
    private String secondName;
    private LocalDate birthdayDate;
    private String interests;
    private String requestForLessons;
    private String diagnosis;
}
