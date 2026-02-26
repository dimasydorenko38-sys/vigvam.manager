package com.sydorenko.vigvam.manager.dto.request.organizations;

import lombok.NonNull;

import java.time.LocalDateTime;

public record CreatePriceRequestDto(
        LocalDateTime activatedDate,
        @NonNull Long serviceTypeId,
        @NonNull String lessonType,
        @NonNull Long valueOnePay,
        Long valueSubscriptionPay
) { }
