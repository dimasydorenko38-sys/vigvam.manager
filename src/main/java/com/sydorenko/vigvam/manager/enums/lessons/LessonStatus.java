package com.sydorenko.vigvam.manager.enums.lessons;

import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;

public enum LessonStatus {

    WAIT,
    DONE,
    DONE_FREE,
    MISSED_FREE,
    MISSED_PAYMENT,
    CANCELED,
    CONSULTATION,
    COMMENTATION,
    ACCOMPANIMENT;

    public static LessonStatus fromString(String statusStr) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(statusStr))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Статус уроку " + statusStr + " не знайдено в системі"));
    }


}
