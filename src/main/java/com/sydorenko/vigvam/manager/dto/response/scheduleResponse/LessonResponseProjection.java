package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LessonResponseProjection {
    Long getId();
    LessonStatus getLessonStatus();
    LessonType getType();
    LessonCategory getCategory();
    LocalDateTime getLessonDateTime();
    LocalDateTime getLessonEndTime();
    String getComments();

    Long getServiceType();
    Long getOrganization();
    Long getEmployee();
    Optional<Long> getChild();

}
