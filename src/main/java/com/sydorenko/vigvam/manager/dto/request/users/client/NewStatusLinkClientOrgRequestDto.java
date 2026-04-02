package com.sydorenko.vigvam.manager.dto.request.users.client;

import lombok.NonNull;

public record NewStatusLinkClientOrgRequestDto(
        @NonNull Long clientId,
        @NonNull Long organizationId
) {
}
