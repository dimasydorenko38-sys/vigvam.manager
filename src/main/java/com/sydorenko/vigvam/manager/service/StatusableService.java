package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public abstract class StatusableService<T extends Statusable> {

    public void setDisableStatus(DisabledObjectRequestDto dto, JpaRepository<T,Long> repository){
        T entity = repository.findById(dto.getId()).orElseThrow(()-> new EntityNotFoundException("Такого об'єкту не істнує в базі"));
        entity.setStatus(Status.DISABLED);
        entity.setDisableDate(LocalDateTime.now());
        repository.save(entity);
    }

    public void setEnableStatus(DisabledObjectRequestDto dto, JpaRepository<T,Long> repository){
        T entity = repository.findById(dto.getId()).orElseThrow(()-> new EntityNotFoundException("Такого об'єкту не істнує в базі"));
        entity.setStatus(Status.ENABLED);
        repository.save(entity);
    }

}
