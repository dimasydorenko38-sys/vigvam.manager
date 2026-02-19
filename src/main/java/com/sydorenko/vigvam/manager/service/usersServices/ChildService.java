package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.configuration.AuditorAwareImpl;
import com.sydorenko.vigvam.manager.dto.request.CreateChildRequestDto;
import com.sydorenko.vigvam.manager.dto.request.NewStatusObjectByIdRequestDto;
import com.sydorenko.vigvam.manager.dto.response.scheduleResponse.ChildNameResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ChildRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientsOrganizationsRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChildService extends StatusableService<ChildEntity> {

    private final ChildRepository childRepository;
    private final AuditorAwareImpl auditorAware;
    private final ClientRepository clientRepository;
    private final ClientsOrganizationsRepository clientsOrganizationsRepository;

    public void setDisableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setDisableStatus(dto.getId(), childRepository);
    }
    public void setEnableStatus(NewStatusObjectByIdRequestDto dto) {
        super.setEnableStatus(dto.getId(), childRepository);
    }

    public void clientCreatesChild(CreateChildRequestDto dto) {
        Long currentClientId =  auditorAware.getCurrentAuditor().orElseThrow(()-> new AuthenticationException("Незареэстрований юзер не може створити профіль для дитини") {
        });
        if(!clientRepository.existsActiveById(currentClientId)){
            throw new EntityNotFoundException("Юзера деактивовано або не існує в базі");
        }
        ChildEntity child = ChildEntity.builder()
                .client(clientRepository.getReferenceById(currentClientId))
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
        if(!clientRepository.existsActiveById(dto.getClientId())){
            throw new EntityNotFoundException("Юзера деактивовано або не існує в базі");
        }
        ChildEntity child = ChildEntity.builder()
                .client(clientRepository.getReferenceById(dto.getClientId()))
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

    public Set<ChildNameResponseDto> getAllChildNameByOrgId(Long organizationId){
        List<ClientsOrganizationsEntity> clientLinks = clientsOrganizationsRepository.findAllByClientStatusAndOrgId(Status.ENABLED,organizationId);
        return clientLinks
                .stream()
                .filter(link -> Status.ENABLED.equals(link.getStatus()))
                .map(ClientsOrganizationsEntity::getClient)
                .flatMap(client -> Stream.ofNullable(client.getChildren()))
                .flatMap(Collection::stream)
                .filter(childEntity -> Status.ENABLED.equals(childEntity.getStatus()))
                .map(ChildNameResponseDto::new)
                .collect(toSet());
    }

}
