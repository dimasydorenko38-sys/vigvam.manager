package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity,Long> {
    Long countByStatusAndIdIn(Status status, Collection<Long> ids);
    boolean existsByIdAndStatus(Long id, Status status);

    default ServiceTypeEntity getActiveRef (Long serviceTypeId){
        if(!existsByIdAndStatus(serviceTypeId, Status.ENABLED)){
            throw new EntityNotFoundException("Послуга не активна або не існує в системі");
        }
        return getReferenceById(serviceTypeId);
    }
}
