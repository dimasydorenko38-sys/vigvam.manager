package com.sydorenko.vigvam.manager.service.organizationsServices;

import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.CreateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.UpdateOrganizationRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService extends StatusableService<OrganizationEntity> {

    private final OrganizationRepository organizationRepository;
    private final SupportOrganizationService supportService;

    public void createOrganizationThisSettings(CreateOrganizationRequestDto dto) {
        if (dto == null || dto.getSettingLessonsTimesList().isEmpty() || dto.getPriceList().isEmpty()) {
            throw new IllegalArgumentException("Дані організації заповнено некоректно!");
        }
        OrganizationEntity newOrganization = new OrganizationEntity();
        newOrganization.setOrganizationName(dto.getOrganizationName());
        newOrganization.setOrganizationCity(dto.getOrganizationCity());
        newOrganization.setAddress(dto.getAddress());
        newOrganization.setStatus(Status.ENABLED);
        newOrganization.setSettingLessons(supportService.convertDtoSettingLessonToEntity(dto.getSettingLessonsTimesList(), newOrganization));
        newOrganization.setPrice(supportService.convertDtoPriceToEntity(dto.getPriceList(), newOrganization));
        supportService.checkSettingTimeAndPrice(newOrganization);
        organizationRepository.save(newOrganization);
    }

    public void updateOrganization(UpdateOrganizationRequestDto dto) {
        OrganizationEntity currentOrg = organizationRepository.findActiveByIdWithSettingsAndPrice(dto.organizationId())
                .orElseThrow(() -> new EntityNotFoundException("Ця організація деактивована, або не існує"));
        currentOrg.setOrganizationName(dto.organizationName());
        currentOrg.setOrganizationCity(dto.organizationCity());
        currentOrg.setAddress(dto.address());
        currentOrg.getSettingLessons().clear();
        currentOrg.getSettingLessons().putAll(supportService.convertDtoSettingLessonToEntity(dto.settingLessonsTimesList(), currentOrg));
        if(!dto.priceList().isEmpty()) {
            currentOrg.getPrice().addAll(supportService.convertDtoPriceToEntity(dto.priceList(), currentOrg));
        }
        supportService.checkSettingTimeAndPrice(currentOrg);
        organizationRepository.save(currentOrg);
    }

    public void setDisableStatus(UpdateStatusObjectByIdRequestDto dto) {
        super.setDisableStatus(dto.getId(), organizationRepository);
    }

    public void setEnableStatus(UpdateStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), organizationRepository);
    }


}
