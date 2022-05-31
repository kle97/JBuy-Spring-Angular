package io.spring.jbuy.features.product.indexer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1/admin/mass-indexing")
@Tag(name = "mass-indexing-admin", description = "mass indexing API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class MassIndexingController {

    private final MassIndexingService massIndexingService;

    @GetMapping("/trigger")
    @Operation(summary = "trigger mass indexing to reindex product", tags = "mass-indexing-admin")
    public ResponseEntity<Void> triggerMassIndexing() throws InterruptedException {
        massIndexingService.massIndexing();
        return ResponseEntity.ok().build();
    }
}
