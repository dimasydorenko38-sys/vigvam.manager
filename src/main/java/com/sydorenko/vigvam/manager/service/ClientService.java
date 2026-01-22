package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.dto.request.CreateClientRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.enums.users.SourceClient;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtService jwtService;


    public String createClient(CreateClientRequestDto dto) {
        ClientEntity client = new ClientEntity();
        client.setStatus(Status.ENABLED);
        client.setRole(RoleUser.CLIENT);
        client.setLogin(dto.getLogin());
        client.setPassword(dto.getPassword());
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        client.setPhotoPermission(dto.isPhotoPermission());
        client.setSource(SourceClient.valueOf(dto.getSourceClient().toUpperCase()));
        client.setOrganizations(Set.of(organizationRepository
                .findById(dto.getOrganization().getId())
                .orElseThrow(() -> new EntityNotFoundException("Такої організації не інснує"))));
        client.setChildren(dto.getChildren()
                .stream()
                .peek(child -> {
                    child.setClient(client);
                    child.setStatus(Status.ENABLED);
                    })
                .collect(Collectors.toSet()));
        if(client.getChildren().isEmpty()){
            throw new IllegalArgumentException("Дані дитини не заповнено");
        }

        clientRepository.save(client);
        return jwtService.generateToken(client);

    }
}
