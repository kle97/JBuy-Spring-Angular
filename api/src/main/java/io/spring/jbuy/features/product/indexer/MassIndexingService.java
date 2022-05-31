package io.spring.jbuy.features.product.indexer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.schema.management.SearchSchemaManager;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor @Slf4j
public class MassIndexingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void massIndexing() throws InterruptedException {
        SearchSession searchSession = Search.session(entityManager);
        SearchSchemaManager schemaManager = searchSession.schemaManager();
        schemaManager.dropAndCreate();
        searchSession.massIndexer().startAndWait();
    }
}
