package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.SalaryEmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

//{
//"employee": {"id": 1},
//"organization": {"id": 1},
//"masterEmployee": null,
//"salary": [
//        {
//            "serviceType": {"id": 1},
//            "value": 150,
//            "premiumValue": 20
//        }
//        ],
//"role": "employee"
//        }


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContractEmployeeRequestDto {

    @NonNull
    private EmployeeEntity employee;
    @NonNull
    private OrganizationEntity organization;

    private EmployeeEntity masterEmployee;
    @NonNull
    private List<SalaryEmployeeEntity> salary;
    @NonNull
    private String role;

}
