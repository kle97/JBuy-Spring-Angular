package io.spring.jbuy.features.product.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PageFacetImpl<T> extends PageImpl<T> implements PageFacet<T> {

    Map<String, List<ProductAttributeFacet>> facetMap;

    public PageFacetImpl(List<T> content,
                         Pageable pageable,
                         long total,
                         Map<String, List<ProductAttributeFacet>> facetMap) {
        super(content, pageable, total);
        this.facetMap = facetMap;
    }
}
