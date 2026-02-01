package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequestDto {
    private String lessonStatus;
    private ChildEntity child;
    private ServiceTypeEntity serviceType;
    @NonNull
    private String lessonType;
    @NonNull
    private LocalDateTime lessonDateTime;
    private LocalDateTime lessonEndTime;
    @NonNull
    private OrganizationEntity organization;
    @NonNull
    private EmployeeEntity employee;
    private String comments;
}
