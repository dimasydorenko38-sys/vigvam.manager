package com.sydorenko.vigvam.manager.dto.request.organizations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {

    @NonNull
    private String organizationName;
    @NonNull
    private String organizationCity;
    @NonNull
    private String address;
    @NonNull
    private List<CreateSettingLessonsTimeRequestDto> settingLessonsTimesList;
    @NonNull
    private List<CreatePriceRequestDto> priceList;
}

