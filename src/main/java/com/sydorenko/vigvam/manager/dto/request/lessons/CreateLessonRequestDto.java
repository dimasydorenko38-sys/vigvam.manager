package com.sydorenko.vigvam.manager.dto.request.lessons;

import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanningLessonDto;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequestDto {
    private String lessonStatus;
    private Long childId;
    private Long serviceTypeId;
    @NonNull
    private String lessonType;
    @NonNull
    private LocalDateTime lessonDateTime;
    private LocalDateTime lessonEndTime;
    @NonNull
    private Long organizationId;
    @NonNull
    private Long employeeId;
    private String comments;
    private LessonCategory lessonCategory;
    private Long parentPlanId;


    public CreateLessonRequestDto(@NonNull PlanningLessonDto planLesson, @NonNull LocalDate date) {
        this.lessonStatus = planLesson.getLessonStatus().name();
        this.childId = planLesson.getChildId();
        this.serviceTypeId = planLesson.getServiceTypeId();
        this.lessonType = planLesson.getLessonType().name();
        if(date.getDayOfWeek().equals(planLesson.getLessonDayOfWeek())){
            this.lessonDateTime = date.atTime(planLesson.getLessonTime());
            this.lessonEndTime = date.atTime(planLesson.getLessonEndTime());
        }else throw new IllegalArgumentException("Цей плановий урок не підходить за днем тижня");
        this.organizationId = planLesson.getOrganizationId();
        this.employeeId = planLesson.getEmployeeId();
        this.comments = planLesson.getComments();
        this.lessonCategory = planLesson.getCategory();
        this.parentPlanId = planLesson.getId();
    }
}
