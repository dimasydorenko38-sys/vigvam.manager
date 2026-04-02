package com.sydorenko.vigvam.manager.dto.request.organizations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {

    @NotBlank(message = "Імʼя обовʼязкове поле")
    private String organizationName;
    @NotBlank(message = "Вкажіть місто організації")
    private String organizationCity;
    @NotBlank(message = "Вкажіть Вулицю та номер будинку")
    private String address;
    @NotEmpty
    private List<CreateSettingLessonsTimeRequestDto> settingLessonsTimesList;
    @NotEmpty
    private List<CreatePriceRequestDto> priceList;
}

