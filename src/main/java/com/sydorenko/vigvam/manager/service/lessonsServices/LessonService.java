package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.configuration.BusinessConfig;
import com.sydorenko.vigvam.manager.dto.request.lessons.CreateLessonByPlanLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.UpdateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.LessonResponseProjection;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.PlanningLessonDto;
import com.sydorenko.vigvam.manager.enums.lessons.LessonCategory;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.GenericService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CheckerLessonService checkerLessonService;
    private final BusinessConfig businessConfig;
    private final PlanningLessonRepository planningLessonRepository;
    private final GenericService genericService;


    public void createGenericLesson(@NonNull CreateLessonRequestDto dto) {
        LessonStatus lessonStatus = checkerLessonService.checkStatus(dto.getLessonStatus(), dto.getLessonDateTime());
        LessonCategory lessonCategory = dto.getLessonCategory();
        if (lessonCategory != LessonCategory.REGULAR) lessonCategory = LessonCategory.WINDOW;
        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntities(dto.getServiceTypeId(), dto.getEmployeeId(), dto.getOrganizationId(), dto.getLessonType(), dto.getChildId());
        LessonEntity lesson = LessonEntity.builder()
                .category(lessonCategory)
                .parentPlanId(dto.getParentPlanId())
                .lessonStatus(lessonStatus)
                .lessonDateTime(dto.getLessonDateTime())
                .lessonEndTime(dto.getLessonEndTime())
                .lessonType(LessonType.fromString(dto.getLessonType()))
                .comments(dto.getComments())
                .serviceType(entitiesForLesson.getServiceType())
                .child(entitiesForLesson.getChild())
                .employee(entitiesForLesson.getEmployee())
                .organization(entitiesForLesson.getOrganization())
                .build();

        lesson = checkerLessonService.check(lesson);
        if (checkerLessonService.checkOverlayOfFactVsPlan(lesson)
                && !businessConfig.getIgnoreLessonStatusesInSchedule().contains(lesson.getLessonStatus()))
            throw new DuplicateRequestException("Існує плановий урок на цей час");
        lessonRepository.save(lesson);
    }

    public void createLessonByPlanLesson(CreateLessonByPlanLessonRequestDto dto) {
        PlanningLessonDto planLesson = new PlanningLessonDto(planningLessonRepository.findById(dto.planLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Не знайдено такого уроку")));
        if (dto.date().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Неможливо перевести план у факт заднім числом. Змініть дату");
        genericService.checkAuditorByOrganization(planLesson.getOrganizationId());
        createGenericLesson(new CreateLessonRequestDto(planLesson, dto.date()));
    }


    public void updateLesson(UpdateLessonRequestDto dto) {
        LessonEntity lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new EntityNotFoundException("Такого уроку не існує. ID: " + dto.getLessonId()));
        LessonStatus lessonStatus = checkerLessonService.checkStatus(dto.getLessonStatus(), dto.getLessonDateTime());

        EntitiesForLesson entitiesForLesson = checkerLessonService.getEntities(dto.getServiceTypeId(), dto.getEmployeeId(), dto.getOrganizationId(), dto.getLessonType(), dto.getChildId());
        if (!lessonStatus.equals(lesson.getLessonStatus())) {
            lesson.setUpdatedLessonStatus(LocalDateTime.now());
        }
        lesson.setLessonStatus(lessonStatus);
        lesson.setLessonDateTime(dto.getLessonDateTime());
        lesson.setLessonEndTime(dto.getLessonEndTime());
        lesson.setLessonType(LessonType.fromString(dto.getLessonType()));
        lesson.setComments(dto.getComments());
        lesson.setServiceType(entitiesForLesson.getServiceType());
        lesson.setChild(entitiesForLesson.getChild());
        lesson.setEmployee(entitiesForLesson.getEmployee());
        lesson.setOrganization(entitiesForLesson.getOrganization());

        lesson = checkerLessonService.check(lesson);
        boolean needCheck = lesson.getLessonDateTime().toLocalDate().isAfter(LocalDate.now().plusDays(1));
        boolean canCheck = !lessonRepository.existsCategoriesLessonsThisDay(
                lesson.getOrganization().getId(),
                lesson.getLessonDateTime().toLocalDate().atStartOfDay(),
                lesson.getLessonEndTime().toLocalDate().atTime(LocalTime.MAX),
                LessonCategory.REGULAR, businessConfig.getCanIgnoreStatusesInOverlayLessons()
        );
        if (needCheck) {
            canCheck = true;
        }
        if (canCheck
                && !businessConfig.getIgnoreLessonStatusesInSchedule().contains(lesson.getLessonStatus())
                && checkerLessonService.checkOverlayOfFactVsPlan(lesson))
            throw new DuplicateRequestException("Існує плановий урок на цей час");
        lessonRepository.save(lesson);
    }

    public List<LessonResponseProjection> getLessonsByOrgIdForPeriod(Long organizationId, LocalDateTime startDate, LocalDateTime endDate) {
        return lessonRepository.findAllByOrgIdForPeriod(organizationId, startDate, endDate, businessConfig.getIgnoreLessonStatusesInSchedule());
    }
}
