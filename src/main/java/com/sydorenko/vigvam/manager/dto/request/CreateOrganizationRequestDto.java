package com.sydorenko.vigvam.manager.dto.request;

import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//                {
//                       "organizationName": "2Організація",
//                       "organizationCity": "Київ",
//                       "settingLessonsTimesList": [
//                       {
//                       "lessonType": "INDIVIDUAL",
//                       "firstHourOfWork": "09:00",
//                       "lastHourOfWork": "18:00",
//                       "lessonDuration": 45,
//                       "breakDuration": 15
//                       },
//                       {
//                       "lessonType": "GROUP",
//                       "firstHourOfWork": "10:00",
//                       "lastHourOfWork": "19:00",
//                       "lessonDuration": 50,
//                       "breakDuration": 10
//                       }
//                       ],
//                       "priceList": [
//                       {
//                       "lessonType": "INDIVIDUAL",
//                       "value": 350
//                       },
//                       {
//                       "lessonType": "GROUP",
//                       "value": 200
//                       }
//                       ]


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequestDto {

    private String organizationName;
    private String organizationCity;
    private List<SettingLessonsTime> settingLessonsTimesList;
    private List<PriceOrganizationEntity> priceList;
}
