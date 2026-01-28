package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.interfaces.Statusable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public abstract class GenericService <T extends Statusable> {

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
