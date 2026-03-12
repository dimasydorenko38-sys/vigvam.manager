package com.sydorenko.vigvam.manager.persistence.repository;


import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
ChildRepository extends JpaRepository<ChildEntity,Long>, GenericActiveRepository<ChildEntity> {
    List<ChildEntity> getReferenceByClientIdAndStatus(@NonNull Long id, Status status);
}
