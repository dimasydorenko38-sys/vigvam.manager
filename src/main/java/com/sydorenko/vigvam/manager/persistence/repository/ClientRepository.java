package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.users.ClientEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends UserRepository<@NonNull ClientEntity>{
    boolean existsByLogin(String login);
}
