package io.spring.jbuy.features.product.indexer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor @Slf4j
public class H2IndexingInitializer {

    private final MassIndexingService massIndexingService;
    @Value("${spring.sql.init.platform}")
    private String initPlatform;

    @EventListener(ContextRefreshedEvent.class)
    public void massIndexingInitializerForH2Database() throws InterruptedException {
        if (initPlatform.equals("h2")) {
            massIndexingService.massIndexing();
            log.info("Hibernate Search mass indexing is completed");
        }
    }
}
