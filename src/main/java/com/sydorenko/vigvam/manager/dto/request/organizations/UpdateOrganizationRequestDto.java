package com.sydorenko.vigvam.manager.dto.request.organizations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;

import java.util.List;

public record UpdateOrganizationRequestDto(
        @NonNull Long organizationId,
        @NotBlank(message = "Назва не можу бути порожнім") String organizationName,
        @NotBlank(message = "Місто необхідно вказати") String organizationCity,
        @NotBlank(message = "Вкажіть адресу організації") String address,
        @NotEmpty List<CreateSettingLessonsTimeRequestDto>settingLessonsTimesList,
        List<CreatePriceRequestDto> priceList
) {
}
