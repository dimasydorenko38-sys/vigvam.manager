package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.arhiv.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
