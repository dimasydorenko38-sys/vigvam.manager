package com.sydorenko.vigvam.manager.dto.request.users.client;

import lombok.NonNull;

public record CreateLinkClientOrgRequestDto(@NonNull Long clientId, @NonNull Long organizationId) {
}
