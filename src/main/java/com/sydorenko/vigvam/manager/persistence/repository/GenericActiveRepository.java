package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface GenericActiveRepository<E> extends JpaRepository<E, Long> {

    default Optional<E> findActiveById(Long id){
        return findByIdAndStatus(id, Status.ENABLED);
    }
    Optional<E> findByIdAndStatus(Long id, Status status);

    default boolean existsActiveById(Long id){
        return existsByIdAndStatus(id, Status.ENABLED);
    }
    boolean existsByIdAndStatus(Long id, Status status);

}
