package com.sydorenko.vigvam.manager.service.organizationsServices;

import com.sydorenko.vigvam.manager.dto.request.organizations.UpdateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.organizations.CreateServiceTypeRequestDto;
import com.sydorenko.vigvam.manager.dto.request.UpdateStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ServiceTypeRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceTypeService extends StatusableService<ServiceTypeEntity> {

    private final ServiceTypeRepository serviceTypeRepository;

    public void createServiceType(CreateServiceTypeRequestDto dto) {
        ServiceTypeEntity serviceTypeEntity = new ServiceTypeEntity();
        serviceTypeEntity.setServiceType(dto.getServiceType().toUpperCase());
        serviceTypeEntity.setDisplayName(dto.getDisplayName());
        serviceTypeEntity.setStatus(Status.ENABLED);
        serviceTypeRepository.save(serviceTypeEntity);
    }

    public void updateServiceType(UpdateServiceTypeRequestDto dto){
        ServiceTypeEntity serviceType = serviceTypeRepository.findActiveById(dto.getServiceTypeId())
                .orElseThrow(()-> new EntityNotFoundException("Ця послуга деактивована або не існує в системі"));
        serviceType.setServiceType(dto.getServiceType());
        serviceType.setDisplayName(dto.getDisplayName());
        serviceTypeRepository.save(serviceType);
    }

    public ServiceTypeEntity getServiceTypeAndCheck(Long id){
        ServiceTypeEntity checkServiceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Послуга відсутня в системі "));
        if(checkServiceType.getStatus() != Status.ENABLED){
            throw new EntityNotFoundException("Послуга " + checkServiceType.getServiceType() +" ("+checkServiceType.getDisplayName()+")" + " вже не активна в системі");
        };
        return checkServiceType;
    }

    public void setDisableStatus(Long serviceId) {
        super.setDisableStatus(serviceId, serviceTypeRepository);
    }

    public void setEnableStatus(Long serviceId) {
        super.setEnableStatus(serviceId, serviceTypeRepository);
    }
}
