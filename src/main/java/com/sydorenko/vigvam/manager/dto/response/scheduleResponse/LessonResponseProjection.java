package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonResponseProjection {
    Long getId();
    LessonStatus getLessonStatus();
    LessonType getLessonType();
    LessonCategory getCategory();
    LocalDateTime getLessonDateTime();
    LocalDateTime getLessonEndTime();
    LocalDateTime getUpdatedLessonStatus();
    String getComments();

    Long getServiceTypeId();
    Long getOrganizationId();
    Long getEmployeeId();
    Optional<Long> getChildId();
}
