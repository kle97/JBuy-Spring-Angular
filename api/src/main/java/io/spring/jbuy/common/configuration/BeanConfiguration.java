package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.spring.jbuy.features.product.dto.PageFacet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class BeanConfiguration {

    private final SystemProperties systemProperties;

    // configuration for custom pagination serialization to apply @JsonView from controller
    @Bean
    public Module paginationWithJsonViewModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Page.class, new CustomPageSerializer());
        module.addSerializer(PageFacet.class, new CustomPageFacetSerializer());
        module.addSerializer(Slice.class, new CustomSliceSerializer());
        return module;
    }

    // configure auditor provider for auditing classes
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new CustomAuditorAware();
    }

    // Set password encoding schema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    // configure CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(systemProperties.getUrls()));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
