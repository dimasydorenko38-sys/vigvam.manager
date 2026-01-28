package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceTypeService extends GenericService<ServiceTypeEntity> {

    private final ServiceTypeRepository serviceTypeRepository;

    public void createServiceType(CreateServiceTypeRequestDto dto) {
        ServiceTypeEntity serviceTypeEntity = new ServiceTypeEntity();
        serviceTypeEntity.setServiceType(dto.getServiceType().toUpperCase());
        serviceTypeEntity.setDisplayName(dto.getDisplayName());
        serviceTypeEntity.setStatus(Status.ENABLED);
        serviceTypeRepository.save(serviceTypeEntity);
    }

    public ServiceTypeEntity getServiceTypeById(Long id){
        ServiceTypeEntity checkServiceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Полуга відсутня в системі "));
        if(checkServiceType.getStatus() != Status.ENABLED){
            throw new EntityNotFoundException("Полуга " + checkServiceType.getServiceType() +" ("+checkServiceType.getDisplayName()+")" + " вже не активна в системі");
        };
        return checkServiceType;
    }

    public void setDisableStatus(DisabledObjectRequestDto dto) {
        super.setDisableStatus(dto, serviceTypeRepository);
    }

    public void setEnableStatus(DisabledObjectRequestDto dto) {
        super.setEnableStatus(dto, serviceTypeRepository);
    }
}
