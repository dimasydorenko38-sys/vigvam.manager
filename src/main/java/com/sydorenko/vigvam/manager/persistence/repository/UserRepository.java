package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface UserRepository<T extends UserEntity> extends JpaRepository<T, Long>, GenericActiveRepository<T> {

    Optional<T> findByRefreshTokenAndStatus(UUID refreshToken, Status status);

    Optional<T> findByLoginAndPasswordAndStatus(String login, String password, Status status);
}

//TODO: findEntity default only status.ENABLED