package com.sydorenko.vigvam.manager.dto.response;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;

public record OrganizationNamesResponseDto(
        Long id,
        String organizationName,
        String organizationCity,
        String address
        ) {
    public OrganizationNamesResponseDto(OrganizationEntity entity){
        this(entity.getId(), entity.getOrganizationName(), entity.getOrganizationCity(), entity.getAddress());
    }
}
