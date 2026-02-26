package com.sydorenko.vigvam.manager.persistence.repository;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.users.SalaryEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<SalaryEmployeeEntity,Long>, GenericActiveRepository<SalaryEmployeeEntity> {
    Long countByStatusAndContractEmployeeIdAndServiceTypeIdInAndActivatedDateAfter(Status enabled, Long contractId, Collection<Long> serviceIds, LocalDateTime activatedDate);

    @Query("""
            select s from SalaryEmployeeEntity s
            where s.status = ?1 and s.contractEmployee.id = ?2 and s.serviceType.id = ?3 and s.lessonType = ?4
            order by s.activatedDate DESC""")
    List<SalaryEmployeeEntity> findAllActiveAnalogsAndSortByActivatedDate(Status status, Long contractID, Long serviceTypeId, LessonType lessonType);
}

