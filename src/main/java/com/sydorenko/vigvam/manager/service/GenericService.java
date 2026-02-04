package com.sydorenko.vigvam.manager.service;

import com.sydorenko.vigvam.manager.configuration.AuditorAwareImpl;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ContractEmployeeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import com.sydorenko.vigvam.manager.persistence.repository.ClientRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ClientsOrganizationsRepository;
import com.sydorenko.vigvam.manager.persistence.repository.ContractEmployeeRepository;
import com.sydorenko.vigvam.manager.persistence.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenericService {

    private final AuditorAwareImpl auditorAware;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientsOrganizationsRepository clientsOrganizationsRepository;
    private final ContractEmployeeRepository contractEmployeeRepository;


    public boolean checkAuditorByOrganization(Long organizationId) {
        Long currentUserID = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new AuthorizationDeniedException("Неможливо визначити Ваш ID, Ваш токен невалідний"));
        Set<RoleUser> currentAuditorRoles = auditorAware.getCurrentAuditorRoles();
        Collection<OrganizationEntity> organizations;
        if (currentAuditorRoles.contains(RoleUser.CLIENT)) {
            organizations = clientsOrganizationsRepository.findAllByClientId(currentUserID)
                    .stream()
                    .map(ClientsOrganizationsEntity::getOrganization)
                    .collect(Collectors.toSet());
        } else {
            organizations = contractEmployeeRepository.findAllWithOrgByEmployeeId(currentUserID)
                    .stream()
                    .map(ContractEmployeeEntity::getOrganization)
                    .collect(Collectors.toSet());
        }
        return organizations.stream().anyMatch(organization -> organization.getId().equals(organizationId));
    }


    public UserEntity getUserByTokenFields(Long userId, Set<RoleUser> roles) {
        if (roles.contains(RoleUser.CLIENT)) {
            return clientRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Клієнта не знайдено"));
        } else {
            return employeeRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Працівника не знайдено"));
        }
    }


}
