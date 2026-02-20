package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String login;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private LocalDate birthday;
}
