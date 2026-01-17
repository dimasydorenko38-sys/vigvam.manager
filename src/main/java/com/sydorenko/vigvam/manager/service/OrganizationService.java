package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final ServiceTypeRepository serviceTypeRepository;

    @Transactional
    public void createOrganizationThisSettings (CreateOrganizationRequestDto dto){
        if(dto == null || dto.getSettingLessonsTimesList().isEmpty() || dto.getPriceList().isEmpty()){
            throw new IllegalArgumentException("Дані організації заповнено некоректно!");
        }
        OrganizationEntity newOrganization = new OrganizationEntity();
        newOrganization.setOrganizationName(dto.getOrganizationName());
        newOrganization.setOrganizationCity(dto.getOrganizationCity());
        newOrganization.setStatus(Status.ENABLED);

        newOrganization.setSettingLessons( dto.getSettingLessonsTimesList()
                .stream()
                .peek(s -> s.setOrganization(newOrganization))
                .collect(toMap(SettingLessonsTime::getLessonType, identity())));
        newOrganization.setPrice(dto.getPriceList()
                .stream()
                .peek(price -> {
                    price.setOrganization(newOrganization);
                    ServiceTypeEntity checkServiceType = serviceTypeRepository.getById(price.getServiceType().getId());
                    if(checkServiceType.getStatus() != Status.ENABLED){
                        throw new EntityNotFoundException("Полуга " + checkServiceType.getServiceType() +" ("+checkServiceType.getDisplayName()+")" + " не існує або ця послуга вимкнена в системі");
                    }
                    price.setServiceType(checkServiceType);
                })
                .collect(toSet()));

        repository.save(newOrganization);
    }
}
