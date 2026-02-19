package com.sydorenko.vigvam.manager.dto.request;

import lombok.NonNull;

public record CreatePriceRequestDto(
        @NonNull Long serviceTypeId,
        @NonNull Long valueOnePay,
        Long valueSubscriptionPay
) { }
