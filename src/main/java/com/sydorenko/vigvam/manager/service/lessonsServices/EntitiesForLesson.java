package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntitiesForLesson {
    private ServiceTypeEntity serviceType;
    private EmployeeEntity employee;
    private OrganizationEntity organization;
    private ChildEntity child;
}
