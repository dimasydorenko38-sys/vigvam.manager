package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class PlanningLessonResponseDto {
    private Long id;
    private LocalTime lessonTime;
    private LocalTime lessonEndTime;
    private DayOfWeek lessonDayOfWeek;
    private LessonStatus lessonStatus;
    private LocalDateTime updatedLessonStatus;
    private Long serviceTypeId;
    private LessonType lessonType;
    private LessonCategory category;
    private Long organizationId;
    private Long employeeId;
    private Long childId;
    private String comments;


    public PlanningLessonResponseDto(PlanningLessonEntity planningLesson) {

        this.id = planningLesson.getId();
        this.lessonTime = planningLesson.getLessonTime();
        this.lessonEndTime = planningLesson.getLessonEndTime();
        this.lessonDayOfWeek = planningLesson.getLessonDayOfWeek();
        this.lessonStatus = planningLesson.getLessonStatus();
        this.updatedLessonStatus = planningLesson.getUpdatedLessonStatus();
        this.serviceTypeId = planningLesson.getServiceType().getId();
        this.lessonType = planningLesson.getLessonType();
        this.category = planningLesson.getCategory();
        this.organizationId = planningLesson.getOrganization().getId();
        this.employeeId = planningLesson.getEmployee().getId();
        this.childId = planningLesson.getChild() != null ? planningLesson.getChild().getId() : null;
        this.comments = planningLesson.getComments();
    }
}
