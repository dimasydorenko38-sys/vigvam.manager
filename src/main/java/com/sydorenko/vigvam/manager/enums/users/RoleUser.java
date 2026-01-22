package com.sydorenko.vigvam.manager.enums.users;

import java.util.Arrays;

public enum RoleUser {
    SUPER_ADMIN,
    ADMIN,
    CLIENT,
    EMPLOYEE,
    MASTER_EMPLOYEE;

    public static RoleUser fromString(String roleStr) {
        return Arrays.stream(values())
                .filter(r -> r.name().equalsIgnoreCase(roleStr))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Роль " + roleStr + " не знайдена"));
    }
}