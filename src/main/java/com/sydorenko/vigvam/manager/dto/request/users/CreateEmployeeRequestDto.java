package com.sydorenko.vigvam.manager.dto.request.users;

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

    @NonNull private String login;
    @NonNull private String password;
    @NonNull private String name;
    @NonNull private String lastName;
    @NonNull private String phone;
    @NonNull private LocalDate birthday;
}
