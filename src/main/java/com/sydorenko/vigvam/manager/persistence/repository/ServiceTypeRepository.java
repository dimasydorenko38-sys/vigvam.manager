package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.persistence.entities.lessons.ServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity,Long> {
}
