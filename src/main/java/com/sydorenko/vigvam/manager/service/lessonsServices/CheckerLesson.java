package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.LessonEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ChildRepository;
import com.sydorenko.vigvam.manager.persistence.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckerLesson {

    private final ChildRepository childRepository;
    private final LessonRepository lessonRepository;

    public LessonEntity check(@NonNull LessonEntity lesson) {
        lesson = validationChild(lesson);
        lesson = checkLessonTime(lesson);
        lesson = checkStausDto(lesson);
        if (checkOverlayOfLessons(lesson)) throw new DuplicateRequestException("Урок на цей час уже існує");
        return lesson;
    }

    public LessonEntity checkStausDto(@NonNull LessonEntity lesson) {
        LocalDate lessonDate = lesson.getLessonDateTime().toLocalDate();
        boolean isPastDate = lessonDate.isBefore(LocalDate.now());
        if (lesson.getLessonStatus() == null) {
            LessonStatus calculatedStatus = isPastDate ? LessonStatus.DONE : LessonStatus.WAIT;
            lesson.setLessonStatus(calculatedStatus);
            return lesson;
        }

        if (isPastDate && lesson.getLessonStatus().equals(LessonStatus.WAIT)) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати");
        } else if (!isPastDate && lesson.getLessonStatus().equals(LessonStatus.DONE)) {
            throw new IllegalArgumentException("Цей статус заняття не доступний для поточної дати ");
        }
        return lesson;
    }


    public @NonNull LessonEntity validationChild(@NonNull LessonEntity lesson) {
        List<LessonStatus> independentChildStatuses = List.of(
                LessonStatus.ACCOMPANIMENT,
                LessonStatus.COMMENTATION,
                LessonStatus.CANCELLED
        );

        if (!independentChildStatuses.contains(lesson.getLessonStatus())
                && lesson.getChild() == null && lesson.getComments() == null) {
            throw new IllegalArgumentException("Необхідно вказати дитину або коментар");
        }

        if (lesson.getChild() != null) {
            ChildEntity childEntity = childRepository.findById(lesson.getChild().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Така дитина не зареэстврована або вимкнена в системы"));
            lesson.setChild(childEntity);
        }
        return lesson;
    }

    public boolean checkOverlayOfLessons(LessonEntity lesson) {
        List<LessonStatus> canIgnoreStatuses = List.of(
                LessonStatus.CANCELLED,
                LessonStatus.MISSED_FREE,
                LessonStatus.MISSED_PAYMENT,
                LessonStatus.COMMENTATION
        );
        List<LessonType> checkTypes = new ArrayList<>(List.of());
        switch (lesson.getType()) {
            case LessonType.INDIVIDUAL -> {
                checkTypes.add(LessonType.GROUP);
                checkTypes.add(LessonType.INDIVIDUAL);
            }
            case LessonType.GROUP -> checkTypes.add(LessonType.INDIVIDUAL);
        }
        return lessonRepository.existsOverlayOfLessons(
                lesson.getOrganization(),
                lesson.getEmployee(),
                checkTypes,
                lesson.getLessonDateTime(),
                lesson.getLessonEndTime(),
                canIgnoreStatuses,
                lesson.getId());
    }


    public @NonNull LessonEntity checkLessonTime(@NonNull LessonEntity lesson) {
        LocalTime lessonTime = lesson.getLessonDateTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        SettingLessonsTime settingLessonsTime = lesson.getOrganization().getSettingLessons().get(lesson.getType());
        if (!settingLessonsTime.getFirstHourOfWork().isBefore(lessonTime)
                && !settingLessonsTime.getLastHourOfWork().isAfter(lessonTime)) {
            throw new IllegalArgumentException("Час уроку виходить за межі робочого графіку організації");
        }
        if (lesson.getLessonEndTime() != null) {
            long durationLesson = Duration.between(lesson.getLessonDateTime(), lesson.getLessonEndTime())
                    .toMinutes();
            if (durationLesson < 0) {
                throw new IllegalArgumentException("Заняття не може починатися після його закінчення");
            } else if (durationLesson > 360) {
                throw new IllegalArgumentException("Заняття не може тривати так довго");
            } else {
                lesson.setLessonEndTime(lesson.getLessonEndTime().truncatedTo(ChronoUnit.MINUTES));
            }
        } else {
            lesson.setLessonEndTime(lesson.getLessonDateTime()
                    .plusMinutes(settingLessonsTime.getLessonDurationMinutes())
                    .truncatedTo(ChronoUnit.MINUTES));
        }
        return lesson;
    }
}
