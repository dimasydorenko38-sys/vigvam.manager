package com.sydorenko.vigvam.manager.dto.request.users;

public record NewStatusLinkClientOrgRequestDto(
        Long clientId,
        Long organizationId
) {
}
