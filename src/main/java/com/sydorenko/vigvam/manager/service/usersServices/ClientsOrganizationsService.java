package com.sydorenko.vigvam.manager.service.usersServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.dto.request.users.CreateLinkClientOrgRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.NewStatusLinkClientOrgRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientsOrganizationsRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientsOrganizationsService extends StatusableService<ClientsOrganizationsEntity> {

    private final ClientsOrganizationsRepository clientsOrganizationsRepository;
    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;


    public void setDisableStatus(NewStatusLinkClientOrgRequestDto dto) {
        ClientsOrganizationsEntity clientsOrganizationsEntity =
                clientsOrganizationsRepository.findByClientIdAndOrganizationId(dto.clientId(),dto.organizationId())
                        .orElseThrow(() -> new EntityNotFoundException("Такого зв'язку не існує"));
        super.setDisableStatus(clientsOrganizationsEntity.getId(), clientsOrganizationsRepository);
    }
    public void setEnableStatus(NewStatusLinkClientOrgRequestDto dto) {
        ClientsOrganizationsEntity clientsOrganizationsEntity =
                clientsOrganizationsRepository.findByClientIdAndOrganizationId(dto.clientId(),dto.organizationId())
                        .orElseThrow(() -> new EntityNotFoundException("Такого зв'язку не існує"));
        super.setEnableStatus(clientsOrganizationsEntity.getId(), clientsOrganizationsRepository);
    }

    public void addLinkClientOrganization(CreateLinkClientOrgRequestDto dto) {
        if(clientsOrganizationsRepository
                .existsByClientIdAndOrganizationIdAndStatus(dto.clientId(), dto.organizationId(), Status.ENABLED)){
            throw new DuplicateRequestException("Цей клієнт вже прив'язаний до обраної організації");
        }
        ClientEntity client = clientRepository.findActiveById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Цей клієнт не існує або вимкнений"));
        OrganizationEntity organization = organizationRepository.findActiveById(dto.organizationId())
                .orElseThrow(()-> new EntityNotFoundException("Організація вимкнена або не існує"));
        ClientsOrganizationsEntity clientsOrganizationsEntity =
                new ClientsOrganizationsEntity(client,organization,Status.ENABLED);
        clientsOrganizationsRepository.save(clientsOrganizationsEntity);
    }
}
