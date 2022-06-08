package io.spring.jbuy.features.product.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface PageFacet<T> extends Page<T> {

    Map<String, List<ProductAttributeFacet>> getFacetMap();
}
