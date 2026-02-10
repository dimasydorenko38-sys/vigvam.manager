package com.sydorenko.vigvam.manager.dto.response.scheduleResponse;

import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrganizationResponseDto {
    private Long id;
    private String organizationName;
    private String organizationCity;
    private String address;
    private Map<LessonType, SettingLessonsTimeResponseDto> settingLessons;


    public OrganizationResponseDto(OrganizationEntity organization) {
        this.id = organization.getId();
        this.organizationName = organization.getOrganizationName();
        this.organizationCity = organization.getOrganizationCity();
        this.address = organization.getAddress();
        this.settingLessons = Optional.ofNullable(organization.getSettingLessons())
                .map(Map::values)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(SettingLessonsTime::getLessonType, SettingLessonsTimeResponseDto::new));
    }
}
