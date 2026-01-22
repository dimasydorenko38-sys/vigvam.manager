package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}
