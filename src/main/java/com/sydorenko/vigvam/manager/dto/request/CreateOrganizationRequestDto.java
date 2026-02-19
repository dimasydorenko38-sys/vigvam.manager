package com.sydorenko.vigvam.manager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {

    private String organizationName;
    private String organizationCity;
    private List<CreateSettingLessonsTimeRequestDto> settingLessonsTimesList;
    private List<CreatePriceRequestDto> priceList;
}

