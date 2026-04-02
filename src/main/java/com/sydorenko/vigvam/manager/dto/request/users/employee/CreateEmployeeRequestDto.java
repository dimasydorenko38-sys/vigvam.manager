package com.sydorenko.vigvam.manager.dto.request.users.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;


//  {
//      "login": "admin",
//      "password": "admin",
//      "name": "UserName",
//      "lastName": "UserLastName",
//      "phone": "Phone"
//        }


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequestDto {

    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phone;
    @NonNull
    private LocalDate birthday;
}
