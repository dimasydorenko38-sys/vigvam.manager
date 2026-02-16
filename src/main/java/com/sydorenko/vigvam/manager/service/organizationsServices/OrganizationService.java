package com.sydorenko.vigvam.manager.service.organizationsServices;

import com.sydorenko.vigvam.manager.dto.request.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class OrganizationService extends StatusableService<OrganizationEntity> {

    private final OrganizationRepository repository;
    private final ServiceTypeService serviceTypeService;


    @Transactional
    public void createOrganizationThisSettings(CreateOrganizationRequestDto dto) {
        if (dto == null || dto.getSettingLessonsTimesList().isEmpty() || dto.getPriceList().isEmpty()) {
            throw new IllegalArgumentException("Дані організації заповнено некоректно!");
        }
        OrganizationEntity newOrganization = new OrganizationEntity();
        newOrganization.setOrganizationName(dto.getOrganizationName());
        newOrganization.setOrganizationCity(dto.getOrganizationCity());
        newOrganization.setStatus(Status.ENABLED);

        newOrganization.setSettingLessons(dto.getSettingLessonsTimesList()
                .stream()
                .peek(s -> {
                    s.getFirstHourOfWork().truncatedTo(ChronoUnit.MINUTES);
                    s.getLastHourOfWork().truncatedTo(ChronoUnit.MINUTES);
                    if (s.getFirstHourOfWork().isAfter(s.getLastHourOfWork())) {
                        throw new IllegalArgumentException("Час початку не може бути пізніше часу завершення");
                    }
                    s.setOrganization(newOrganization);
                }).collect(toMap(SettingLessonsTime::getLessonType, identity())));

        newOrganization.setPrice(dto.getPriceList()
                .stream()
                .peek(price -> {
                    price.setOrganization(newOrganization);
                    price.setServiceType(serviceTypeService.getServiceTypeAndCheck(price.getServiceType().getId()));
                }).collect(toSet()));

        repository.save(newOrganization);
    }

    public void setDisableStatus(DisabledObjectRequestDto dto) {
        super.setDisableStatus(dto, repository);
    }

    public void setEnableStatus(DisabledObjectRequestDto dto) {
        super.setEnableStatus(dto, repository);
    }
}
