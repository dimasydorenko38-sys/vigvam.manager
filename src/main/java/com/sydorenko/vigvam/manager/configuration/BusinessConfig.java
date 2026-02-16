package com.sydorenko.vigvam.manager.configuration;

import com.sydorenko.vigvam.manager.enums.lessons.LessonStatus;
import lombok.Getter;
import org.springframework.stereotype.Component;

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
}
