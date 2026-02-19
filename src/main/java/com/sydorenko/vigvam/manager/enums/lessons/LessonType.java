package com.sydorenko.vigvam.manager.enums.lessons;

import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;

public enum LessonType {
    INDIVIDUAL,
    GROUP;

    public static LessonType fromString(String typeStr) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(typeStr))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Тип уроку " + typeStr + " не знайдено"));
    }

}
