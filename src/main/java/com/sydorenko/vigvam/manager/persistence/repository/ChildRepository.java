package com.sydorenko.vigvam.manager.persistence.repository;


import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity,Long>, GenericActiveRepository<ChildEntity> {
}
