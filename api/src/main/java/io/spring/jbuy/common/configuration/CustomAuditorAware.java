package io.spring.jbuy.common.configuration;

import io.spring.jbuy.features.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * AuditorAware indicates current principal for properties annotated with @CreatedBy or @LastModifiedBy
 */
public class CustomAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            return Optional.of(user.getId());
        } else if (principal instanceof UUID) {
            return Optional.of((UUID) principal);
        }

        return Optional.empty();
    }
}
