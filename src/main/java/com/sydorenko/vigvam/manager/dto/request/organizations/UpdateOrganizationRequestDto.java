package com.sydorenko.vigvam.manager.dto.request.organizations;

import lombok.NonNull;

import java.util.List;

public record UpdateOrganizationRequestDto(
        @NonNull Long organizationId,
        @NonNull String organizationName,
        @NonNull String organizationCity,
        @NonNull String address,
        @NonNull List<CreateSettingLessonsTimeRequestDto>settingLessonsTimesList,
        List<CreatePriceRequestDto> priceList
) {
}
