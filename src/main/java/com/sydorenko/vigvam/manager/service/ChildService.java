package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.configuration.AuditorAwareImpl;
import com.sydorenko.vigvam.manager.dto.request.CreateChildRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ChildRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChildService extends GenericService<ChildEntity> {

    private final ChildRepository childRepository;
    private final AuditorAwareImpl auditorAware;
    private final ClientRepository clientRepository;


    public void setDisableStatus(DisabledObjectRequestDto dto) {
        super.setDisableStatus(dto, childRepository);
    }
    public void setEnableStatus(DisabledObjectRequestDto dto) {
        super.setEnableStatus(dto, childRepository);
    }


    public void clientCreatesChild(CreateChildRequestDto dto) {
        Long currentClientId =  auditorAware.getCurrentAuditor().orElseThrow(()-> new AuthenticationException("Незареэстрований юзер не може створити профіль для дитини") {
        });
        ClientEntity currentClient = clientRepository.findById(currentClientId)
                .orElseThrow(()->new EntityNotFoundException("Юзер не існує в базі"));
        ChildEntity child = ChildEntity.builder()
                .client(currentClient)
                .name(dto.getName())
                .lastName(dto.getLastName())
                .secondName(dto.getSecondName())
                .birthdayDate(dto.getBirthdayDate())
                .diagnosis(dto.getDiagnosis())
                .interests(dto.getInterests())
                .requestForLessons(dto.getRequestForLessons())
                .status(Status.ENABLED)
                .build();
        childRepository.save(child);
    }

    public void adminCreatesChild(CreateChildRequestDto dto) {
        ClientEntity currentClient = clientRepository.findById(dto.getClient().getId())
                .orElseThrow(()->new EntityNotFoundException("Такого клієнта не існує в базі"));
        ChildEntity child = ChildEntity.builder()
                .client(currentClient)
                .name(dto.getName())
                .lastName(dto.getLastName())
                .secondName(dto.getSecondName())
                .birthdayDate(dto.getBirthdayDate())
                .diagnosis(dto.getDiagnosis())
                .interests(dto.getInterests())
                .requestForLessons(dto.getRequestForLessons())
                .status(Status.ENABLED)
                .build();
        childRepository.save(child);

    }
}
