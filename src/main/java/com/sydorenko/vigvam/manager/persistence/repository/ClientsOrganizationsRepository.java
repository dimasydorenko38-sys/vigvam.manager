package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ClientsOrganizationsEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientsOrganizationsRepository extends JpaRepository<ClientsOrganizationsEntity, LessonRepository> {
    @EntityGraph(attributePaths = {"organization"})
    List<ClientsOrganizationsEntity> findAllByClientId(Long clientId);
}
