package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    @Transactional
    public void createServiceType(CreateServiceTypeRequestDto dto) {
        ServiceTypeEntity serviceTypeEntity = new ServiceTypeEntity();
        serviceTypeEntity.setServiceType(dto.getServiceType().toUpperCase());
        serviceTypeEntity.setDisplayName(dto.getDisplayName());
        serviceTypeEntity.setStatus(Status.ENABLED);
        serviceTypeRepository.save(serviceTypeEntity);
    }
}
