package com.sydorenko.vigvam.manager.service.usersServices;

import com.sun.jdi.request.DuplicateRequestException;
import com.sydorenko.vigvam.manager.dto.request.users.client.UpdateClientRequestDto;
import com.sydorenko.vigvam.manager.dto.request.users.client.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ChildRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientsOrganizationsRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import com.sydorenko.vigvam.manager.service.GenericService;
import com.sydorenko.vigvam.manager.service.JwtService;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService extends StatusableService<ClientEntity> {

    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtService jwtService;
    private final ClientsOrganizationsRepository clientsOrganizationsRepository;
    private final ClientsOrganizationsService clientsOrganizationsService;
    private final GenericService genericService;
    private final ChildRepository childRepository;
    private final ChildService childService;


    public AuthResponseDto createClient(CreateClientRequestDto dto, boolean isCreatedByClient) {
        if (clientRepository.existsByLogin(dto.getLogin())) {
            throw new DuplicateRequestException("Цей логін або номер телефону вже використовується в системі, вигадайте новий логін для реєстрації");
        }
        ClientEntity client = new ClientEntity();
        client.setStatus(Status.ENABLED);
        client.setRole(RoleUser.CLIENT);
        client.setLogin(dto.getLogin().toLowerCase());
        client.setPassword(dto.getPassword());
        client.setName(dto.getName());
        client.setPhone(genericService.formatPhone(dto.getPhone()));
        client.setPhotoPermission(dto.isPhotoPermission());
        client.setSource(SourceClient.valueOf(dto.getSourceClient().toUpperCase()));
        client.setCreatedByClient(isCreatedByClient);
        if (!organizationRepository.existsActiveById(dto.getOrganizationId())) {
            throw new EntityNotFoundException("Такої організації не існує");
        }
        OrganizationEntity currentOrganization = organizationRepository.getReferenceById(dto.getOrganizationId());
        client.setOrganizationLinks(Set.of(new ClientsOrganizationsEntity(client, currentOrganization, Status.ENABLED)));
        client.setChildren(dto.getChildren()
                .stream()
                .map(child -> ChildEntity.builder()
                        .client(client)
                        .secondName(child.getSecondName())
                        .birthdayDate(child.getBirthdayDate())
                        .diagnosis(child.getDiagnosis())
                        .name(child.getName())
                        .interests(child.getInterests())
                        .lastName(child.getLastName())
                        .requestForLessons(child.getRequestForLessons())
                        .status(Status.ENABLED)
                        .build()
                )
                .collect(Collectors.toSet()));
        if (client.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Дані дитини не заповнено");
        }
        ClientEntity saveClient = clientRepository.save(client);
        UUID refreshToken = saveClient.getRefreshToken();
        String token = jwtService.generateToken(saveClient);
        return new AuthResponseDto(token, refreshToken);
    }

    public void updateClient(UpdateClientRequestDto dto) {
        ClientEntity client = clientRepository.findActiveById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Клієнта деактивовано або не існує в системі"));
        client.setName(dto.getName());
        client.setPhone(genericService.formatPhone(dto.getPhone()));
        client.setLogin(dto.getLogin());
        client.setPassword(dto.getPassword());
        clientRepository.save(client);
    }

    public void setDisableStatusCascade(Long clientId) {
        List<ClientsOrganizationsEntity> linkList = clientsOrganizationsRepository.getReferenceByClientIdAndStatus(clientId, Status.ENABLED);
        if (!linkList.isEmpty()) {
            linkList.forEach(link -> clientsOrganizationsService.setDisableStatus(link.getId(), clientsOrganizationsRepository));
        }
        List<ChildEntity> childrenIds = childRepository.getReferenceByClientIdAndStatus(clientId, Status.ENABLED);
        if(!childrenIds.isEmpty()){
            childrenIds.forEach(child -> childService.setDisableStatus(child.getId()));
        }
        super.setDisableStatus(clientId, clientRepository);
    }

    public void setEnableStatus(Long id) {
        super.setEnableStatus(id, clientRepository);
    }

    public Set<OrganizationEntity> getAllOrganizations(Long clientId) {
        List<ClientsOrganizationsEntity> links = clientsOrganizationsRepository.findAllActiveWithOrgByClientId(clientId);
        if (links == null || links.isEmpty()) {
            throw new IllegalArgumentException("Цей клієнт не прив'язаний до жодної організації");
        }
        Set<OrganizationEntity> organizations = links.stream()
                .map(ClientsOrganizationsEntity::getOrganization)
                .filter(org -> Status.ENABLED.equals(org.getStatus()))
                .collect(Collectors.toSet());
        if (!organizations.isEmpty()) {
            return organizations;
        } else throw new EntityNotFoundException("Не знайдено жодної активної організації з клієнтом");
    }
}
