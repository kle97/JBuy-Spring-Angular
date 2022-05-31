package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor @Slf4j
public class OpenAPIConfiguration {

    private final SystemProperties systemProperties;
    private final ApplicationContext applicationContext;

    // configuration for Springdoc to pickup Spring ObjectMapper (in case of changing property naming strategy)
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/api/v1/**"};
        String[] pathsToExclude = {"/api/v1/admin/**"};

        GroupedOpenApi.Builder userApiBuilder = GroupedOpenApi.builder()
                .group("jbuy-user")
                .pathsToMatch(paths)
                .pathsToExclude(pathsToExclude)
                .addOpenApiCustomiser(rapidocCollapseTagByDefault());

        return userApiBuilder.build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        String[] paths = {"/api/v1/admin/**"};
        String[] packagesToScan = {};
        GroupedOpenApi.Builder adminApiBuilder = GroupedOpenApi.builder()
                .group("jbuy-admin")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .addOpenApiCustomiser(openApi -> openApi.addSecurityItem(new SecurityRequirement().addList("httpBasic")))
                .addOpenApiCustomiser(rapidocCollapseTagByDefault());

        return adminApiBuilder.build();
    }

    @Bean
    public OpenApiCustomiser adminSecurityRequirement() {
        return openApi -> openApi.getPaths().values().stream()
                .flatMap(pathItem -> pathItem.readOperations().stream())
                .filter(operation -> operation.getTags().size() > 0
                        && operation.getTags().get(0).contains("admin"))
                .forEach(operation -> operation
                        .addSecurityItem(new SecurityRequirement().addList("httpBasic")));
    }

    // Springdoc bean configuration
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("JBuy API")
                              .version(systemProperties.getCurrentVersion()).description("JBuy.")
                              .license(new License().name("MIT").url(
                                      "https://github.com/kle97/JBuy-Spring-Angular/blob/master/LICENSE"))
                )
                .addServersItem(new Server().description("Development Server").url(systemProperties.getUrls()[0]))
                .components(new Components().addSecuritySchemes("httpBasic",
                                                                new SecurityScheme()
                                                                        .type(SecurityScheme.Type.HTTP)
                                                                        .scheme("basic")));
    }

    @Bean
    public OpenApiCustomiser rapidocCollapseTagByDefault() {
        return openApi -> openApi.getTags().forEach(tag -> tag.addExtension("x-tag-expanded", false));
    }

}

