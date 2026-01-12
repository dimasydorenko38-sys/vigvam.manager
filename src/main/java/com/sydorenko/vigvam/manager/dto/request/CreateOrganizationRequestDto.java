package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//                    {
//                            "organizationName": "3Організація",
//                            "organizationCity": "Київ",
//                            "settingLessonsTimesList": [
//                            {
//                            "lessonType": "INDIVIDUAL",
//                            "firstHourOfWork": "09:00",
//                            "lastHourOfWork": "18:00",
//                            "lessonDuration": 45,
//                            "breakDuration": 15
//                            },
//                            {
//                            "lessonType": "GROUP",
//                            "firstHourOfWork": "10:00",
//                            "lastHourOfWork": "19:00",
//                            "lessonDuration": 50,
//                            "breakDuration": 10
//                            }
//                            ],
//                            "priceList": [
//                            {
//                            "serviceType":  { "id": 1 },
//                            "value": 350
//                            },
//                            {
//                            "serviceType":  { "id": 2 },
//                            "value": 400
//                            }
//                            ]
//                    }



@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {

    private String organizationName;
    private String organizationCity;
    private List<SettingLessonsTime> settingLessonsTimesList;
    private List<PriceOrganizationEntity> priceList;
}

