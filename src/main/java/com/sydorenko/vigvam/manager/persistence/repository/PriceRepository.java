package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.PriceOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceOrganizationEntity, Long>,GenericActiveRepository<PriceOrganizationEntity> {
    @Query("""
            select s from PriceOrganizationEntity s
            where s.status = ?1 and s.organization.id = ?2 and s.serviceType.id = ?3 and s.lessonType = ?4
            order by s.activatedDate DESC""")
    List<PriceOrganizationEntity> findAllActiveAnalogsAndSortByActivatedDate(Status status, Long organizationID, Long serviceTypeId, LessonType lessonType);
}

