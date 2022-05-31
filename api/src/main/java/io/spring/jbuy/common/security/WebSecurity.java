package io.spring.jbuy.common.security;

import io.spring.jbuy.features.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("webSecurity") @Slf4j
public class WebSecurity {

    public boolean hasUserId(UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) authentication.getPrincipal()).getId().equals(userId);
            } else if (principal instanceof UUID) {
                return authentication.getPrincipal().equals(userId);
            }
        }

        return false;
    }

    public boolean hasCustomerId(UUID customerId) {
        return hasUserId(customerId);
    }
}
