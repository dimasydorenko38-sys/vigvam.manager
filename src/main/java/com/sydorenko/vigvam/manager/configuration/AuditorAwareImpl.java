package com.sydorenko.vigvam.manager.configuration;

import com.sydorenko.vigvam.manager.enums.Status;
import com.sydorenko.vigvam.manager.enums.users.RoleUser;
import com.sydorenko.vigvam.manager.persistence.entities.users.UserEntity;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @NonNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String userId = jwtToken.getToken().getClaimAsString("sub");
            return Optional.ofNullable(userId).map(Long::valueOf);
        }

        return Optional.empty();
    }

    @NonNull
    public Set<RoleUser> getCurrentAuditorRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String stringRoles = jwtToken.getToken().getClaimAsString("scope");
            return Arrays.stream(stringRoles.split(" "))
                    .map(RoleUser::fromString)
                    .collect(Collectors.toSet());
        }else try {
            throw new AccessDeniedException("Не існує списку ролей");
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }



}
