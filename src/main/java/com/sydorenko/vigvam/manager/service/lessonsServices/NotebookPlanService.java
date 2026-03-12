package com.sydorenko.vigvam.manager.service.lessonsServices;

import com.sydorenko.vigvam.manager.dto.request.lessons.CreateNotebookPlanScheduleRequestDto;
import com.sydorenko.vigvam.manager.dto.request.lessons.UpdateNotebookPlanScheduleRequestDto;
import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.lessons.LessonType;
import com.sydorenko.vigvam.manager.persistence.entities.lessons.NotebookPlanScheduleEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.OrganizationEntity;
import com.sydorenko.vigvam.manager.persistence.entities.organizations.ServiceTypeEntity;
import com.sydorenko.vigvam.manager.persistence.entities.users.ChildEntity;
import com.sydorenko.vigvam.manager.persistence.repository.*;
import com.sydorenko.vigvam.manager.service.StatusableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotebookPlanService extends StatusableService<NotebookPlanScheduleEntity> {

    private final NotebookPlanRepository notebookPlanRepository;
    private final ChildRepository childRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final OrganizationRepository organizationRepository;
    private final ClientsOrganizationsRepository clientsOrganizationsRepository;

    public void createNotebookPlanSchedule(CreateNotebookPlanScheduleRequestDto dto) {
        ChildEntity child = childRepository.findActiveById(dto.childId())
                .orElseThrow(() -> new EntityNotFoundException("Це профіль деактивовано або не існує в системі"));
        OrganizationEntity organization = organizationRepository.getReferenceActiveById(dto.organizationId())
                .orElseThrow(() -> new EntityNotFoundException("Організація деактивована або не існує"));

        if (!clientsOrganizationsRepository
                .existsByClientIdAndOrganizationIdAndStatus(child.getClient().getId(), organization.getId(), Status.ENABLED)) {
            throw new IllegalArgumentException("Цей профіль не зареєстровано за цією організацією");
        }

        FieldsForNote fields = getFieldsForNote(dto.message(), dto.startDate(), dto.endDate(), dto.lessonTypeList(), dto.serviceTypeIds());

        NotebookPlanScheduleEntity notebookPlanSchedule = NotebookPlanScheduleEntity.builder()
                .child(child)
                .organization(organization)
                .message(fields.message())
                .status(Status.ENABLED)
                .startDate(dto.startDate().truncatedTo(ChronoUnit.MINUTES))
                .endDate(fields.endDate().truncatedTo(ChronoUnit.MINUTES))
                .lessonTypeList(fields.lessonTypeList())
                .serviceTypeIds(fields.serviceTypeList())
                .build();

        notebookPlanRepository.save(notebookPlanSchedule);
    }

    private FieldsForNote getFieldsForNote(String message, LocalDateTime startDate, LocalDateTime endDate, List<String> lessonTypeList, Set<Long> serviceTypeIds) {
        if (message.length() < 5) {
            throw new IllegalArgumentException("Коментар має бути довшим");
        }
        if (endDate == null) {
            endDate = startDate.toLocalDate().atTime(LocalTime.MAX);
        } else if (endDate.toLocalTime().isBefore(LocalTime.of(7, 0))) {
            endDate = endDate.toLocalDate().atTime(LocalTime.MAX);
        } else if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Дата завершення не можу бут перед початком дії запису");
        }

        List<LessonType> lessonTypes = lessonTypeList.stream()
                .map(LessonType::fromString)
                .toList();
        if (!serviceTypeIds.isEmpty()) {
            Set<ServiceTypeEntity> serviceTypeList = serviceTypeRepository
                    .getReferenceByIdInAndStatus(serviceTypeIds, Status.ENABLED);
            if (serviceTypeList.size() != serviceTypeIds.size()) {
                throw new EntityNotFoundException("Деякі з вказаних послуг не активні або не існують");
            }
        }
        return new FieldsForNote(message, endDate, lessonTypes, serviceTypeIds);
    }


    private record FieldsForNote(String message, LocalDateTime endDate, List<LessonType> lessonTypeList,
                                 Set<Long> serviceTypeList) {
    }

    public void updateNotebookPlanSchedule(UpdateNotebookPlanScheduleRequestDto dto) {
        NotebookPlanScheduleEntity notebookPlanSchedule = notebookPlanRepository.findActiveById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Записку деактивовано або вона не існує"));

        FieldsForNote fields = getFieldsForNote(dto.message(), dto.startDate(), dto.endDate(), dto.lessonTypeList(), dto.serviceTypeIds());
        notebookPlanSchedule.setMessage(fields.message);
        notebookPlanSchedule.setStartDate(dto.startDate());
        notebookPlanSchedule.setEndDate(fields.endDate);
        notebookPlanSchedule.setLessonTypeList(fields.lessonTypeList);
        notebookPlanSchedule.setServiceTypeIds(fields.serviceTypeList);
        notebookPlanRepository.save(notebookPlanSchedule);
    }

    public void setDisableStatus(Long notebookPlanScheduleId) {
        super.setDisableStatus(notebookPlanScheduleId, notebookPlanRepository);
    }

    public void setEnableStatus(Long organizationId) {
        super.setEnableStatus(organizationId, notebookPlanRepository);
    }
}
