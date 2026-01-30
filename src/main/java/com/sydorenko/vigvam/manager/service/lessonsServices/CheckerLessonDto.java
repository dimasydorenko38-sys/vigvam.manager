package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.dto.request.CreateLessonRequestDto;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ChildRepository;
import com.sydorenko.vigvam.manager.persistence.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class CheckerLessonDto {

    private final ChildRepository childRepository;
    private final LessonRepository lessonRepository;


    public CreateLessonRequestDto checkStaus(@NonNull CreateLessonRequestDto dto) {
        LocalDate lessonDate = dto.getLessonDateTime().toLocalDate();
        boolean isPastDate = lessonDate.isBefore(LocalDate.now());

        if (dto.getLessonStatus() == null) {
            LessonStatus calculatedStatus = isPastDate ? LessonStatus.DONE : LessonStatus.WAIT;
            dto.setLessonStatus(calculatedStatus.name());
            return dto;
        }

        if (isPastDate && dto.getLessonStatus().toUpperCase().equals(LessonStatus.WAIT.name())) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати");
        } else if (!isPastDate && dto.getLessonStatus().toUpperCase().equals(LessonStatus.DONE.name())) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати ");
        }
        return dto;
    }

    public void checkChildIsExist(@NonNull CreateLessonRequestDto dto) {
        List<LessonStatus> independentChildStatus = List.of(
                LessonStatus.ACCOMPANIMENT,
                LessonStatus.COMMENTATION,
                LessonStatus.CANCELLED
        );

        if (!independentChildStatus.contains(LessonStatus.valueOf(dto.getLessonType().toUpperCase()))
                && dto.getChild() == null && dto.getComments() == null)
            throw new IllegalArgumentException("Необхідно вказати дитину або коментар");
    }

    public @NonNull CreateLessonRequestDto validationChild(@NonNull CreateLessonRequestDto dto) {
        if (dto.getChild() != null) {
            ChildEntity childInLesson = childRepository.findById(dto.getChild().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Така дитина не зареэстврована або вимкнена в системы"));
            dto.setChild(childInLesson);
        }
        return dto;
    }

    public boolean checkDublicateLesson(LessonEntity lesson) {
            return lessonRepository.existsByOrganizationAndEmployeeAndLessonDateTimeAndType(lesson.getOrganization(), lesson.getEmployee(), lesson.getLessonDateTime(), LessonType.INDIVIDUAL);
//            TODO: перевір нахльост по заняттям...  додай кінець заняття за замовчуванням організації
    }

    public void checkLessonTime(@NonNull CreateLessonRequestDto dto, OrganizationEntity organization) {
        LocalTime lessonTime = dto.getLessonDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        SettingLessonsTime settingLessonsTime = organization.getSettingLessons().get(LessonType.valueOf(dto.getLessonType().toUpperCase()));
        if(!settingLessonsTime.getFirstHourOfWork().isBefore(lessonTime)
        && !settingLessonsTime.getLastHourOfWork().isAfter(lessonTime)){
            throw new IllegalArgumentException("Час уроку виходить за межі робочого графіку організації");
        }
    }

    public @NonNull CreateLessonRequestDto checkEndLesson(@NonNull CreateLessonRequestDto dto, OrganizationEntity organization) {
        if(dto.getLessonEndTime() != null) {
            dto.setLessonEndTime(dto.getLessonEndTime().truncatedTo(ChronoUnit.MINUTES));
        }else {
            SettingLessonsTime settingLessonsTime = organization.getSettingLessons().get(LessonType.valueOf(dto.getLessonType().toUpperCase()));
            dto.setLessonEndTime(dto.getLessonDateTime().toLocalTime()
                    .plusMinutes(settingLessonsTime.getLessonDurationMinutes())
                    .truncatedTo(ChronoUnit.MINUTES));
        }
        return dto;
    }
}
