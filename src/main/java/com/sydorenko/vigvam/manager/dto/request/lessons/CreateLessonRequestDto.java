package com.sydorenko.vigvam.manager.dto.request.lessons;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonRequestDto {
    private String lessonStatus;
    private Long childId;
    private Long serviceTypeId;
    @NonNull
    private String lessonType;
    @NonNull
    private LocalDateTime lessonDateTime;
    private LocalDateTime lessonEndTime;
    @NonNull
    private Long organizationId;
    @NonNull
    private Long employeeId;
    private String comments;
}
