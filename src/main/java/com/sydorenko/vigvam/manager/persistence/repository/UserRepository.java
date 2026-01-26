package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface UserRepository<T extends UserEntity> extends JpaRepository<T, Long> {
    Optional<T> findByRefreshToken(UUID refreshToken);

    Optional<T> findByLogin(String login);
}
