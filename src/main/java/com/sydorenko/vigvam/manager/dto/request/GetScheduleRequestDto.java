package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetScheduleRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private OrganizationEntity organization;
}
