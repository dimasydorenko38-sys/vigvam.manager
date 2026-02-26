package com.sydorenko.vigvam.manager.service.organizationsServices;

import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.CreatePriceRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.CreateSettingLessonsTimeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.SettingLessonsTime;
import com.sydorenko.vigvam.manager.persistence.repository.PriceRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class SupportOrganizationService extends StatusableService<PriceOrganizationEntity> {

    private final ServiceTypeRepository serviceTypeRepository;
    private final PriceRepository priceRepository;

    public Map<LessonType, SettingLessonsTime> convertDtoSettingLessonToEntity
            (List<CreateSettingLessonsTimeRequestDto> settingDtoList, OrganizationEntity organization) {
        return settingDtoList.stream()
                .peek(s -> {
                            if (s.firstHourOfWork().truncatedTo(ChronoUnit.MINUTES)
                                    .isAfter(s.lastHourOfWork().truncatedTo(ChronoUnit.MINUTES)))
                                throw new IllegalArgumentException("Час початку не може бути пізніше часу завершення");
                        }
                )
                .map(s ->
                        SettingLessonsTime.builder()
                                .firstHourOfWork(s.firstHourOfWork().truncatedTo(ChronoUnit.MINUTES))
                                .lastHourOfWork(s.lastHourOfWork().truncatedTo(ChronoUnit.MINUTES))
                                .lessonDurationMinutes(s.lessonDurationMinutes())
                                .breakDurationMinutes(s.breakDurationMinutes())
                                .organization(organization)
                                .lessonType(LessonType.fromString(s.lessonType()))
                                .build()
                ).collect(toMap(SettingLessonsTime::getLessonType, identity()));
    }

    @Transactional
    public List<PriceOrganizationEntity> convertDtoPriceToEntity(List<CreatePriceRequestDto> priceDtoList, OrganizationEntity organization) {
        Collection<Long> serviceTypeIds = priceDtoList.stream()
                .map(CreatePriceRequestDto::serviceTypeId)
                .collect(toSet());
        if (serviceTypeRepository.countByStatusAndIdIn(Status.ENABLED, serviceTypeIds) < serviceTypeIds.size()) {
            throw new IllegalArgumentException("Деякі види послуг не існують в системі, або вже неактивні.");
        }

        return priceDtoList
                .stream()
//                .peek(price -> {
//                    if (price.activatedDate().truncatedTo(ChronoUnit.MINUTES).isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
//                        throw new IllegalArgumentException("Неможливо змінити прайс організації заднім числом");
//                    }
//                })
                .map(price -> {
                    LocalDateTime date = price.activatedDate();
                    if (date != null && date.truncatedTo(ChronoUnit.MINUTES)
                            .isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
                        throw new IllegalArgumentException("Неможливо змінити прайс організації заднім числом");
                    }
                            return PriceOrganizationEntity.builder()
                                    .organization(organization)
                                    .activatedDate(checkCreatedDate(price.activatedDate()))
                                    .valueOnePay(price.valueOnePay())
                                    .valueSubscriptionPay(price.valueSubscriptionPay())
                                    .serviceType(serviceTypeRepository.getActiveRef(price.serviceTypeId()))
                                    .lessonType(LessonType.fromString(price.lessonType()))
                                    .status(Status.ENABLED)
                                    .build();
                        }
                ).toList();

    }

    public void checkSettingTimeAndPrice(OrganizationEntity newOrganization) {
        if (newOrganization.getSettingLessons().size() >
                newOrganization.getPrice()
                        .stream()
                        .collect(groupingBy(PriceOrganizationEntity::getLessonType))
                        .size()
        ) {
            throw new IllegalArgumentException("Якийсь з типів уроку в налаштунках годин не має прайсу");
        }
    }

    private LocalDateTime checkCreatedDate(LocalDateTime createdDate) {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        return createdDate;
    }

    public void setDisableStatusPrice(UpdateStatusObjectByIdRequestDto dto) {
        PriceOrganizationEntity price = priceRepository.findActiveById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Цей запис деактивовано або не існує в системі"));
        Long organizationID = price.getOrganization().getId();
        Long serviceTypeId = price.getServiceType().getId();
        LessonType lessonType = price.getLessonType();
        List<PriceOrganizationEntity> priceAnalogList = priceRepository
                .findAllActiveAnalogsAndSortByActivatedDate(
                        Status.ENABLED, organizationID, serviceTypeId, lessonType);
        if (priceAnalogList.size() < 2) {
            throw new IllegalArgumentException("Ви не можете вимкнути єдиний діючий запис");
        }
        if (priceAnalogList.getFirst().getId().equals(dto.getId())) {
            price.setStatus(Status.DISABLED);
            priceRepository.save(price);
        } else throw new IllegalArgumentException("Деактивувати можливо лише останній запис цієї категорії ПРАЙСУ");
    }

    public void setEnableStatusPrice(UpdateStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), priceRepository);
    }

}