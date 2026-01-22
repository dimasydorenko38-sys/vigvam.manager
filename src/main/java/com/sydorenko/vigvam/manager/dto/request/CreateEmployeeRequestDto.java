package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


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
}
