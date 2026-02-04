package com.sydorenko.vigvam.manager.service.usersServices;

import com.sydorenko.vigvam.manager.dto.request.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.dto.request.DisabledObjectRequestDto;
import com.sydorenko.vigvam.manager.dto.response.AuthResponseDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientsOrganizationsRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
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


    public AuthResponseDto createClient(CreateClientRequestDto dto) {
        ClientEntity client = new ClientEntity();
        client.setStatus(Status.ENABLED);
        client.setRole(RoleUser.CLIENT);
        client.setLogin(dto.getLogin());
        client.setPassword(dto.getPassword());
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        client.setPhotoPermission(dto.isPhotoPermission());
        client.setSource(SourceClient.valueOf(dto.getSourceClient().toUpperCase()));
        OrganizationEntity currentOrganization = organizationRepository
                .findById(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Такої організації не існує"));
        client.setOrganizationLinks(Set.of(new ClientsOrganizationsEntity(client, currentOrganization, Status.ENABLED)));
        client.setChildren(dto.getChildren()
                .stream()
                .peek(child -> {
                    child.setClient(client);
                    child.setStatus(Status.ENABLED);
                })
                .collect(Collectors.toSet()));
        if (client.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Дані дитини не заповнено");
        }
        ClientEntity saveClient = clientRepository.save(client);
        UUID refreshToken = saveClient.getRefreshToken();
        String token = jwtService.generateToken(saveClient);
        return new AuthResponseDto(token, refreshToken);

    }

    public void setDisableStatus(DisabledObjectRequestDto dto) {
        super.setDisableStatus(dto, clientRepository);
    }

    public void setEnableStatus(DisabledObjectRequestDto dto) {
        super.setEnableStatus(dto, clientRepository);
    }

    public Set<OrganizationEntity> getAllOrganizations(ClientEntity client) {
        List<ClientsOrganizationsEntity> links = clientsOrganizationsRepository.findAllByClientId(client.getId());
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
