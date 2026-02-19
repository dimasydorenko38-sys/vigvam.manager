package com.sydorenko.vigvam.manager.configuration;

import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class BusinessConfig {

    private final List<LessonStatus> ignoreLessonStatusesInSchedule = List.of(
            LessonStatus.CANCELLED
    );

    private final List<LessonStatus> independentOfChildStatuses = List.of(
            LessonStatus.ACCOMPANIMENT,
            LessonStatus.COMMENTATION,
            LessonStatus.CANCELLED
    );

    private final List<LessonStatus> canIgnoreStatusesInOverlayLessons = List.of(
            LessonStatus.CANCELLED,
            LessonStatus.MISSED_FREE,
            LessonStatus.MISSED_PAYMENT,
            LessonStatus.COMMENTATION
    );

    public List<LessonType> getTypesForCheckOfOverlayOfLessons(LessonType lessonType) {
        List<LessonType> checkTypes = new ArrayList<>(List.of());
        switch (lessonType) {
            case LessonType.INDIVIDUAL -> {
                checkTypes.add(LessonType.GROUP);
                checkTypes.add(LessonType.INDIVIDUAL);
            }
            case LessonType.GROUP -> checkTypes.add(LessonType.INDIVIDUAL);
        }
        return checkTypes;
    }
}
