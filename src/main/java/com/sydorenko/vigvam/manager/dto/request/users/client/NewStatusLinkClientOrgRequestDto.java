package com.sydorenko.vigvam.manager.dto.request.users.client;

public record NewStatusLinkClientOrgRequestDto(
        Long clientId,
        Long organizationId
) {
}
