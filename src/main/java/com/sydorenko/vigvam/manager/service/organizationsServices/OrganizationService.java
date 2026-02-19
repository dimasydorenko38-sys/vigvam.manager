package com.sydorenko.vigvam.manager.service.organizationsServices;

import com.sydorenko.vigvam.manager.dto.request.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
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
    private final ServiceTypeRepository serviceTypeRepository;


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
                            if (s.firstHourOfWork().truncatedTo(ChronoUnit.MINUTES)
                                    .isAfter(s.lastHourOfWork().truncatedTo(ChronoUnit.MINUTES)))
                                throw new IllegalArgumentException("Час початку не може бути пізніше часу завершення");
                        }
                )
                .map(s ->
                        SettingLessonsTime.builder()
                                .firstHourOfWork(s.lastHourOfWork().truncatedTo(ChronoUnit.MINUTES))
                                .lastHourOfWork(s.lastHourOfWork().truncatedTo(ChronoUnit.MINUTES))
                                .lessonDurationMinutes(s.lessonDurationMinutes())
                                .breakDurationMinutes(s.breakDurationMinutes())
                                .organization(newOrganization)
                                .lessonType(LessonType.fromString(s.lessonType()))
                                .build()
                ).collect(toMap(SettingLessonsTime::getLessonType, identity())));

        newOrganization.setPrice(dto.getPriceList()
                .stream()
                .map(price -> PriceOrganizationEntity.builder()
                        .organization(newOrganization)
                        .valueOnePay(price.valueOnePay())
                        .valueSubscriptionPay(price.valueSubscriptionPay())
                        .serviceType(serviceTypeRepository.getActiveRef(price.serviceTypeId()))
                        .build()
                ).collect(toSet()));
        repository.save(newOrganization);
    }

    public void setDisableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setDisableStatus(dto.getId(), repository);
    }

    public void setEnableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), repository);
    }
}
