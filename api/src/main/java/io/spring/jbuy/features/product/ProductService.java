package io.spring.jbuy.features.product;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.category.Category;
import io.spring.jbuy.features.keyword.Keyword;
import io.spring.jbuy.features.product.dto.PageFacet;
import io.spring.jbuy.features.product.dto.PageFacetImpl;
import io.spring.jbuy.features.product.dto.ProductAttributeFacet;
import io.spring.jbuy.features.product.dto.ProductDetailResponse;
import io.spring.jbuy.features.product.dto.ProductRequest;
import io.spring.jbuy.features.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.util.common.data.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor @Slf4j
public class ProductService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public Set<String> searchTextAutoComplete(String searchText) {
        SearchSession searchSession = Search.session(entityManager);
        List<String> categoryList = searchSession.search(Category.class)
                .select(f -> f.field("name", String.class))
//                .where(f -> f.simpleQueryString().field("autocompleteCategory").matching(searchText))
                .where(f -> f.bool(b -> {
                    b.must(f.match().fields("autocompleteCategory").matching(searchText).fuzzy(1));
                }))
                .fetchHits(2);

        List<String> brandList = searchSession.search(Product.class)
                .select(f -> f.field("brand", String.class))
//                .where(f -> f.simpleQueryString().field("autocompleteBrand").matching(searchText))
                .where(f -> f.bool(b -> {
                    b.must(f.match().fields("autocompleteBrand").matching(searchText).fuzzy(1));
                }))
                .fetchHits(5);

        List<String> keywordList = searchSession.search(Keyword.class)
                .select(f -> f.field("name", String.class))
//                .where(f -> f.simpleQueryString().field("name").matching(searchText))
                .where(f -> f.bool(b -> {
                    b.must(f.match().fields("name").matching(searchText).fuzzy(1));
                }))
                .fetchHits(20);

        List<List<String>> attributeValueList = searchSession.search(Product.class)
                .select(f -> f.field("attributeValue", String.class).multi())
//                .where(f -> f.simpleQueryString().field("attributeValue").matching(searchText))
                .where(f -> f.bool(b -> {
                    b.must(f.match().fields("attributeValue").matching(searchText).fuzzy(1));
                }))
                .fetchHits(10);

        Set<String> returnList = new LinkedHashSet<>(categoryList);
        returnList.addAll(brandList);
        returnList.addAll(keywordList);

        for (List<String> innerList : attributeValueList) {
            for (String attributeValue : innerList) {
                attributeValue = attributeValue.toLowerCase().trim();
                int index = attributeValue.indexOf(searchText);
                if (index != -1) {
                    returnList.add(attributeValue);
                }
            }
        }
        return returnList;
    }

    @Transactional
    public Page<ProductResponse> searchSimilarProduct(Pageable pageable, String searchText) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<List<?>> productResult = searchSession.search(Product.class)
                .select(f -> f.composite(
                        f.field("id", UUID.class),
                        f.field("brand", String.class),
                        f.field("name", String.class),
                        f.field("regularPrice", Double.class),
                        f.field("discountPrice", Double.class),
                        f.field("stock", Integer.class),
                        f.field("images", String.class),
                        f.field("averageRating", Double.class),
                        f.field("ratingCount", Integer.class)
                ))
                .where(f -> f.bool(b -> {
                    b.must(f.match().fields("listOfCategory.name", "name",
                                            "description", "productAttribute").matching(searchText));
                }))
                .sort(f -> f.composite(b -> {
                    b.add(f.score());
                }))
//                .loading(o -> o.cacheLookupStrategy(
//                        EntityLoadingCacheLookupStrategy.PERSISTENCE_CONTEXT_THEN_SECOND_LEVEL_CACHE
//                ))
                .fetch((int) pageable.getOffset(), pageable.getPageSize());

        List<ProductResponse> listOfProductResponse = new ArrayList<>();
        for (List<?> resultList : productResult.hits()) {
            listOfProductResponse.add(new ProductResponse((UUID) resultList.get(0), // id
                                                          (String) resultList.get(1), // brand
                                                          (String) resultList.get(2), // name
                                                          (Double) resultList.get(3), // regular price
                                                          (Double) resultList.get(4), // discount price
                                                          (Integer) resultList.get(5), // stock
                                                          (String) resultList.get(6), // images
                                                          (Double) resultList.get(7), // average rating
                                                          (Integer) resultList.get(8))); // rating count
        }

        return new PageImpl<>(listOfProductResponse, pageable, productResult.total().hitCount());
    }

    @Transactional
    public PageFacet<ProductResponse> searchProduct(Pageable pageable,
                                                    String searchText,
                                                    @Nullable Boolean dealFilter,
                                                    @Nullable String categoryFilter,
                                                    @Nullable List<String> brandFilter,
                                                    @Nullable List<String> priceFilter,
                                                    @Nullable Integer ratingFilter,
                                                    @Nullable List<String> attributeFilter) {

        SearchSession searchSession = Search.session(entityManager);
        AggregationKey<Map<String, Long>> countsByCategoryKey = AggregationKey.of("countsByCategory");
        AggregationKey<Map<String, Long>> countsByBrandKey = AggregationKey.of("countsByBrand");
        AggregationKey<Map<String, Long>> countsByAttributeKey = AggregationKey.of("countsByAttribute");
        AggregationKey<Map<Range<Double>, Long>> countsByPriceKey = AggregationKey.of("countsByPrice");
        AggregationKey<Map<Range<Double>, Long>> countsByRatingKey = AggregationKey.of("countsByRating");
        AggregationKey<Map<Boolean, Long>> countsByDealKey = AggregationKey.of("countsByDeal");

        List<String> categoryRefinedList = searchSession.search(Category.class)
                .select(f -> f.field("name", String.class))
                .where(f -> f.bool(b -> {
                    b.must(f.match().field("normalizedCategory").matching(searchText).fuzzy());
                }))
                .fetchHits(1);

//        log.info(String.valueOf(categoryRefinedList.size()));

        SearchResult<List<?>> productResult = searchSession.search(Product.class)
                .select(f -> f.composite(
                        f.field("id", UUID.class),
                        f.field("brand", String.class),
                        f.field("name", String.class),
                        f.field("regularPrice", Double.class),
                        f.field("discountPrice", Double.class),
                        f.field("stock", Integer.class),
                        f.field("images", String.class),
                        f.field("averageRating", Double.class),
                        f.field("ratingCount", Integer.class)
                ))
                .where(buildPredicate(searchSession,
                                      searchText,
                                      false,
                                      false,
                                      categoryRefinedList,
                                      dealFilter,
                                      categoryFilter,
                                      brandFilter,
                                      priceFilter,
                                      ratingFilter,
                                      attributeFilter))
                .aggregation(countsByCategoryKey, f -> f.terms()
                        .field("listOfCategory.name", String.class)
                        .maxTermCount(1000)
                        .orderByTermAscending())
                .aggregation(countsByRatingKey, f -> f.range()
                        .field("averageRating", Double.class)
                        .range(1.0, 5.0)
                        .range(2.0, 5.0)
                        .range(3.0, 5.0)
                        .range(4.0, 5.0))
                .aggregation(countsByAttributeKey, f -> f.terms()
                        .field("productAttribute", String.class)
                        .maxTermCount(1000)
                        .orderByTermAscending())
                .aggregation(countsByDealKey, f -> f.terms()
                        .field("discounted", Boolean.class)
                        .maxTermCount(1000))
                .aggregation(countsByBrandKey, f -> f.terms()
                        .field("brand", String.class)
                        .maxTermCount(1000)
                        .orderByTermAscending())
                .aggregation(countsByPriceKey, f -> f.range()
                        .field("discountPrice", Double.class)
                        .range(0.0, 25.0)
                        .range(25.0, 50.0)
                        .range(50.0, 75.0)
                        .range(75.0, 100.0)
                        .range(100.0, 200.0)
                        .range(200.0, 300.0)
                        .range(300.0, 400.0)
                        .range(400.0, 500.0)
                        .range(500.0, 750.0)
                        .range(750.0, 1000.0)
                        .range(1000.0, 1250.0)
                        .range(1250.0, 1500.0)
                        .range(1500.0, 2000.0)
                        .range(2000.0, null))
                .sort(f -> f.composite(b -> {
                    if (pageable.getSort().isSorted()) {
                        String sortField = pageable.getSort().toList().get(0).getProperty();
                        String sortDirection = pageable.getSort().toList().get(0).getDirection().name();
                        SortOrder sortOrder = sortDirection.equals("ASC") ? SortOrder.ASC : SortOrder.DESC;
                        switch (sortField) {
                            case "price":
                                b.add(f.field("discountPrice").order(sortOrder));
                            case "rating":
                                b.add(f.field("averageRating").order(sortOrder));
                            default:
                                b.add(f.score());
                        }
                    } else {
                        b.add(f.score());
                    }
                }))
//                .loading(o -> o.cacheLookupStrategy(
//                        EntityLoadingCacheLookupStrategy.PERSISTENCE_CONTEXT_THEN_SECOND_LEVEL_CACHE
//                ))
                .fetch((int) pageable.getOffset(), pageable.getPageSize());

        List<ProductResponse> listOfProductResponse = new ArrayList<>();
        for (List<?> resultList : productResult.hits()) {
            listOfProductResponse.add(new ProductResponse((UUID) resultList.get(0), // id
                                                          (String) resultList.get(1), // brand
                                                          (String) resultList.get(2), // name
                                                          (Double) resultList.get(3), // regular price
                                                          (Double) resultList.get(4), // discount price
                                                          (Integer) resultList.get(5), // stock
                                                          (String) resultList.get(6), // images
                                                          (Double) resultList.get(7), // average rating
                                                          (Integer) resultList.get(8))); // rating count
        }

        Map<String, Long> countsByBrand;
        if (brandFilter != null && !brandFilter.isEmpty()) {
            SearchResult<List<?>> brandAggregationFetch = searchSession.search(Product.class)
                    .select(f -> f.composite(f.field("id", UUID.class)))
                    .where(buildPredicate(searchSession,
                                          searchText,
                                          true,
                                          false,
                                          categoryRefinedList,
                                          dealFilter,
                                          categoryFilter,
                                          brandFilter,
                                          priceFilter,
                                          ratingFilter,
                                          attributeFilter))
                    .aggregation(countsByBrandKey, f -> f.terms()
                            .field("brand", String.class)
                            .maxTermCount(1000)
                            .orderByTermAscending())
                    .fetch(1);
            countsByBrand = brandAggregationFetch.aggregation(countsByBrandKey);
        } else {
            countsByBrand = productResult.aggregation(countsByBrandKey);
        }

        Map<Range<Double>, Long> countsByPrice;
        if (priceFilter != null && !priceFilter.isEmpty()) {
            SearchResult<List<?>> priceAggregationFetch = searchSession.search(Product.class)
                    .select(f -> f.composite(f.field("id", UUID.class)))
                    .where(buildPredicate(searchSession,
                                          searchText,
                                          false,
                                          true,
                                          categoryRefinedList,
                                          dealFilter,
                                          categoryFilter,
                                          brandFilter,
                                          priceFilter,
                                          ratingFilter,
                                          attributeFilter))
                    .aggregation(countsByPriceKey, f -> f.range()
                            .field("discountPrice", Double.class)
                            .range(0.0, 25.0)
                            .range(25.0, 50.0)
                            .range(50.0, 75.0)
                            .range(75.0, 100.0)
                            .range(100.0, 200.0)
                            .range(200.0, 300.0)
                            .range(300.0, 400.0)
                            .range(400.0, 500.0)
                            .range(500.0, 750.0)
                            .range(750.0, 1000.0)
                            .range(1000.0, 1250.0)
                            .range(1250.0, 1500.0)
                            .range(1500.0, 2000.0)
                            .range(2000.0, null))
                    .fetch(1);
            countsByPrice = priceAggregationFetch.aggregation(countsByPriceKey);
        } else {
            countsByPrice = productResult.aggregation(countsByPriceKey);
        }


        Map<String, Long> countsByCategory = productResult.aggregation(countsByCategoryKey);
        Map<Range<Double>, Long> countsByRating = productResult.aggregation(countsByRatingKey);
        Map<String, Long> countsByAttribute = productResult.aggregation(countsByAttributeKey);
        Map<Boolean, Long> countsByDeal = productResult.aggregation(countsByDealKey);

        Map<String, List<ProductAttributeFacet>> facetMap = new LinkedHashMap<>();

        List<ProductAttributeFacet> dealList = new ArrayList<>();
        for (Map.Entry<Boolean, Long> entry : countsByDeal.entrySet()) {
            if (!entry.getKey()) {
                continue;
            }
            boolean checked = dealFilter != null && dealFilter;
            dealList.add(new ProductAttributeFacet("Deal Only", entry.getValue(), checked));
        }
        facetMap.put("Deals", dealList);

        List<ProductAttributeFacet> categoryList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : countsByCategory.entrySet()) {
            boolean checked = entry.getKey().equals(categoryFilter);
            categoryList.add(new ProductAttributeFacet(entry.getKey(), entry.getValue(), checked));
        }
        facetMap.put("Category", categoryList);

        List<ProductAttributeFacet> brandList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : countsByBrand.entrySet()) {
            boolean checked = false;
            if (brandFilter != null) {
                String key = entry.getKey().toUpperCase();
                for (String brand : brandFilter) {
                    if (brand.toUpperCase().equals(key)) {
                        checked = true;
                        break;
                    }
                }
            }
            brandList.add(new ProductAttributeFacet(entry.getKey(), entry.getValue(), checked));
        }
        facetMap.put("Brand", brandList);

        List<ProductAttributeFacet> priceList = new ArrayList<>();
        List<String> parsedPriceFilter = new ArrayList<>();
        if (priceFilter != null && !priceFilter.isEmpty()) {
            for (String price : priceFilter) {
                Pair<Double, Double> pricePair = this.parsePriceFilter(price);
                String lowerValue = "$" + pricePair.getFirst().intValue();
                String upperValue = pricePair.getSecond() < 999999999
                        ? " to $" + pricePair.getSecond().intValue()
                        : " and Up";
                parsedPriceFilter.add(lowerValue + upperValue);
            }
        }
        for (Map.Entry<Range<Double>, Long> entry : countsByPrice.entrySet()) {
            Range<Double> key = entry.getKey();
            String lowerValue = key.lowerBoundValue().isPresent()
                    ? "$" + key.lowerBoundValue().get().intValue()
                    : "null";
            String upperValue = key.upperBoundValue().isPresent()
                    ? " to $" + key.upperBoundValue().get().intValue()
                    : " and Up";
            boolean checked = parsedPriceFilter.contains(lowerValue + upperValue);
            priceList.add(new ProductAttributeFacet(lowerValue + upperValue, entry.getValue(), checked));
        }
        facetMap.put("Price", priceList);

        List<ProductAttributeFacet> ratingList = new ArrayList<>();
        for (Map.Entry<Range<Double>, Long> entry : countsByRating.entrySet()) {
            Range<Double> key = entry.getKey();
            String rating = key.lowerBoundValue().isPresent()
                    ? key.lowerBoundValue().get().intValue()
                    + (key.lowerBoundValue().get().intValue() > 1 ? " Stars and Up" : " Star and Up")
                    : "null";
            boolean checked = Integer.valueOf(key.lowerBoundValue().get().intValue()).equals(ratingFilter);
            ratingList.add(new ProductAttributeFacet(rating, entry.getValue(), checked));
        }
        facetMap.put("Rating", ratingList);

        for (Map.Entry<String, Long> entry : countsByAttribute.entrySet()) {
            String key = entry.getKey();
            String[] splitKey = key.split("\\|");
            String[] attributeKey = splitKey[1].split("__");
            String attributeType = attributeKey[0];
            String attribute = attributeKey[1];
            String value = attributeKey.length > 2 ? attributeKey[2] : "";

            List<ProductAttributeFacet> attributeList = facetMap.getOrDefault(attribute, new ArrayList<>());

            boolean checked = attributeFilter != null && attributeFilter.contains(value);
            attributeList.add(new ProductAttributeFacet(value, entry.getValue(), checked));
            facetMap.put(attribute, attributeList);
        }

//        for (Map.Entry<String, Long> entry : countsByAttribute.entrySet()) {
//            log.info(entry.getKey() + ": " + entry.getValue());
//        }

        return new PageFacetImpl<>(listOfProductResponse,
                                   pageable,
                                   productResult.total().hitCount(),
                                   facetMap);
    }

    private SearchPredicate buildPredicate(SearchSession searchSession,
                                           String searchText,
                                           boolean aggregateBrandOnly,
                                           boolean aggregatePriceOnly,
                                           List<String> categoryRefinedList,
                                           @Nullable Boolean dealFilter,
                                           @Nullable String categoryFilter,
                                           @Nullable List<String> brandFilter,
                                           @Nullable List<String> priceFilter,
                                           @Nullable Integer ratingFilter,
                                           @Nullable List<String> attributeFilter) {
        SearchPredicateFactory f = searchSession.scope(Product.class).predicate();
        return f.bool(b -> {
            if (dealFilter != null && dealFilter) {
                b.must(f.match().field("discounted").matching(true));
            }
            if (categoryFilter != null && !categoryFilter.isBlank()) {
                b.must(f.match().field("listOfCategory.name").matching(categoryFilter));
            }
            if (!aggregateBrandOnly && brandFilter != null && brandFilter.size() > 0) {
                b.must(fi -> fi.bool(bi -> {
                    for (String brand : brandFilter) {
                        bi.should(f.match().field("brand").matching(brand.toUpperCase()));
                    }
                }));
            }
            if (attributeFilter != null && attributeFilter.size() > 0) {
                b.must(fi -> fi.bool(bi -> {
                    for (String attribute : attributeFilter) {
                        bi.must(f.match().field("productAttribute").matching(attribute.toLowerCase()));
                    }
                }));
            }
            if (!aggregatePriceOnly && priceFilter != null && priceFilter.size() > 0) {
                b.must(fi -> fi.bool(bi -> {
                    for (String price : priceFilter) {
                        Pair<Double, Double> pricePair = this.parsePriceFilter(price);
                        bi.should(f.range().field("discountPrice")
                                          .between(pricePair.getFirst(), pricePair.getSecond()));
                    }
                }));
            }
            if (ratingFilter != null && ratingFilter >= 1 && ratingFilter < 5) {
                b.must(f.range().field("averageRating").atLeast(ratingFilter.doubleValue()));
            }
            if (categoryRefinedList.size() == 1) {
                b.must(f.match().field("listOfCategory.name").matching(categoryRefinedList.get(0)));
            } else {
                b.must(f.match().fields("name", "description", "productAttribute").matching(searchText));
            }
        }).toPredicate();
    }

    @Transactional
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, productId));
    }

    @Transactional
    public ProductResponse getProductResponseById(UUID productId) {
        return productMapper.toProductResponse(getProductById(productId));
    }

    @Transactional
    public Product getProductDetailById(UUID productId) {
        return productRepository.findWithRelationshipsById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, productId));
    }

    @Transactional
    public ProductDetailResponse getProductDetailResponseById(UUID productId) {
        return productMapper.toProductDetailResponse(getProductDetailById(productId));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductResponsePageable(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponse);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product transientProduct = productMapper.toProduct(productRequest);
        transientProduct.setDiscounted(transientProduct.getDiscountPrice() < transientProduct.getRegularPrice());
        return productMapper.toProductResponse(productRepository.save(transientProduct));
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest productRequest) {
        Product currentProduct = getProductById(productId);
        currentProduct.setDiscounted(currentProduct.getDiscountPrice() < currentProduct.getRegularPrice());
        return productMapper.toProductResponse(productMapper.toExistingProduct(productRequest, currentProduct));
    }

    @Transactional
    public void deleteById(UUID productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ResourceNotFoundException(Product.class, productId);
        }
    }

    private Pair<Double, Double> parsePriceFilter(String priceFilter) {
        // valid syntax example: 450-1000
        try {
            String[] priceBounds = priceFilter.split("-");
            double lowerBound = Double.parseDouble(priceBounds[0]);
            double upperBound = priceBounds.length > 1 ? Double.parseDouble(priceBounds[1]) : 999999999;
            if (lowerBound >= upperBound) {
                throw new ValidationException(
                        "Price filter invalid syntax: lower bound must be less than upper bound!");
            }
            return Pair.of(lowerBound, upperBound);
        } catch (PatternSyntaxException ex) {
            throw new ValidationException("Price filter invalid syntax: missing separator '-'!");
        } catch (NullPointerException | NumberFormatException ex) {
            throw new ValidationException("Price filter invalid syntax: not a integer value!");
        }
    }

}
