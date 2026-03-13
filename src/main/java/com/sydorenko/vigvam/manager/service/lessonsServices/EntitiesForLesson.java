package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanningLessonResponseDto;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntitiesForLesson {
    private ServiceTypeEntity serviceType;
    private EmployeeEntity employee;
    private OrganizationEntity organization;
    private ChildEntity child;

    public EntitiesForLesson(PlanningLessonResponseDto plan, OrganizationEntity organization, Map<Long, EmployeeEntity> employeeEntityList, Map<Long, ChildEntity> childEntityList, Map<Long, ServiceTypeEntity> serviceTypeEntityList) {
        this.organization = organization;
        this.employee = employeeEntityList.get(plan.getEmployeeId());
        this.serviceType = serviceTypeEntityList.get(plan.getServiceTypeId());
        this.child = childEntityList.get(plan.getChildId());
    }
}
