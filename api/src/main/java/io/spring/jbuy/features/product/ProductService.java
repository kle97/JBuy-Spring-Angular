package io.spring.jbuy.features.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor @Slf4j
public class ProductService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ProductRepository productRepository;

    @Transactional
    public List<Product> searchProduct(String searchText) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> productResult = searchSession.search(Product.class)
                .where(f -> f.match()
                        .fields("name", "description")
                        .matching(searchText))
                .fetch(20);

        long totalHitCount = productResult.total().hitCount();

        return productResult.hits();
    }

}
