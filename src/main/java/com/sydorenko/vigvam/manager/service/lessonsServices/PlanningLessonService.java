package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.request.lessons.PlanningLessonRequestDto;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.PlanningLessonEntity;
import com.sydorenko.vigvam.manager.persistence.repository.PlanningLessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlanningLessonService {

    private final CheckerLessonService checkerLessonService;
    private final PlanningLessonRepository planningLessonRepository;
    private final BusinessConfig businessConfig;

    public void createPlanningLesson(@NonNull PlanningLessonRequestDto dto) {
        String lessonStatus = dto.status();
        if (lessonStatus == null || lessonStatus.isEmpty()) {
            lessonStatus = "wait";
        }
        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntities(dto.serviceTypeId(), dto.employeeId(), dto.organizationId(), dto.type(), dto.childId());

        PlanningLessonEntity planningLesson = PlanningLessonEntity.builder()
                .lessonStatus(LessonStatus.fromString(lessonStatus))
                .lessonTime(dto.lessonTime())
                .lessonEndTime(dto.lessonEndTime())
                .lessonDayOfWeek(dto.lessonDayOfWeek())
                .lessonType(LessonType.fromString(dto.type()))
                .category(LessonCategory.REGULAR)
                .serviceType(entitiesForLesson.getServiceType())
                .child(entitiesForLesson.getChild())
                .employee(entitiesForLesson.getEmployee())
                .organization(entitiesForLesson.getOrganization())
                .comments(dto.comments())
                .build();
        planningLesson = checkerLessonService.checkLessonTime(planningLesson);
        planningLesson = checkerLessonService.validationChild(planningLesson);
        if (checkerLessonService.checkOverlayOfPlaningLessons(planningLesson))
            throw new DuplicateRequestException("Урок на цей час уже існує");
        planningLessonRepository.save(planningLesson);
    }

    public void updatePlanningLesson(@NonNull PlanningLessonRequestDto dto) {
        PlanningLessonEntity planningLesson = planningLessonRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Цей урок не знайдено в системі"));
        String lessonStatus = dto.status();
        if (!businessConfig.getStatusesCanBeInPlanSchedule().contains(LessonStatus.fromString(lessonStatus))) {
            throw new IllegalArgumentException("Обраний статус уроку не може існувати в плановому графіку");
        }
        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntities(dto.serviceTypeId(), dto.employeeId(), dto.organizationId(), dto.type(), dto.childId());

        if(!LessonStatus.fromString(lessonStatus).equals(planningLesson.getLessonStatus())){
            planningLesson.setUpdatedLessonStatus(LocalDateTime.now());
        }

        planningLesson.setLessonStatus(LessonStatus.fromString(lessonStatus));
        planningLesson.setLessonTime(dto.lessonTime());
        planningLesson.setLessonEndTime(dto.lessonEndTime());
        planningLesson.setLessonDayOfWeek(dto.lessonDayOfWeek());
        planningLesson.setLessonType(LessonType.fromString(dto.type()));
        planningLesson.setCategory(LessonCategory.REGULAR);
        planningLesson.setServiceType(entitiesForLesson.getServiceType());
        planningLesson.setChild(entitiesForLesson.getChild());
        planningLesson.setEmployee(entitiesForLesson.getEmployee());
        planningLesson.setOrganization(entitiesForLesson.getOrganization());
        planningLesson.setComments(dto.comments());

        planningLesson = checkerLessonService.checkLessonTime(planningLesson);
        planningLesson = checkerLessonService.validationChild(planningLesson);
        if (checkerLessonService.checkOverlayOfPlaningLessons(planningLesson))
            throw new DuplicateRequestException("Урок на цей час уже існує");
        planningLessonRepository.save(planningLesson);
    }

    public List<PlanningLessonEntity> getLessonsByOrgIdForPeriod(Long organizationId, Set<DayOfWeek> dayOfWeekSet) {
        return planningLessonRepository.findAllByOrgIdForPeriod(organizationId ,dayOfWeekSet, businessConfig.getIgnoreLessonStatusesInSchedule());
    }
}
