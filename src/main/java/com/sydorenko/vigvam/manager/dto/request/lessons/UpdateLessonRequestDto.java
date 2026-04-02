package com.sydorenko.vigvam.manager.dto.request.lessons;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonRequestDto {
    @NonNull
    private Long lessonId;
    @NotBlank(message = "Статус уроку обовʼязковий")
    private String lessonStatus;
    private Long childId;
    @NonNull
    private Long serviceTypeId;
    @NotBlank
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
