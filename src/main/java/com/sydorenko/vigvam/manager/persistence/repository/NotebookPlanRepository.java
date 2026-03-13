package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.NotebookPlanScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotebookPlanRepository extends JpaRepository<NotebookPlanScheduleEntity, Long>, GenericActiveRepository<NotebookPlanScheduleEntity> {
    @Query("""
            select n from NotebookPlanScheduleEntity n
            where n.organization.id = :orgId
            and n.startDate <= :endPeriod
            and n.endDate >= :startPeriod
            and n.status = :status
            """)
    List<NotebookPlanScheduleEntity> findAllByOrgIdAndPeriodAndStatus(
            @Param("orgId") Long orgId,
            @Param("endPeriod") LocalDateTime endPeriod,
            @Param("startPeriod") LocalDateTime startPeriod,
            @Param("status") Status status);
}