package io.spring.jbuy.features.product.brigde;

import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;

import java.util.List;

public class ProductAttributeBinder implements PropertyBinder {

    @Override
    public void bind(PropertyBindingContext context) {
        context.dependencies()
                .use("value");

        IndexFieldReference<String> attributeValueField = context.indexSchemaElement()
                .field("attributeValue",
                       f -> f.asString()
                               .analyzer("autocomplete_indexing")
                               .searchAnalyzer("autocomplete_search")
                               .projectable(Projectable.YES))
                .multiValued()
                .toReference();

        context.dependencies()
                .use("value")
                .use("attribute.name")
                .use("attribute.attributeType.name");

        IndexFieldReference<String> productAttributeField = context.indexSchemaElement()
                .field("productAttribute",
                       f -> f.asString().normalizer("delimiter").aggregable(Aggregable.YES))
                .multiValued()
                .toReference();

        context.bridge(List.class, new ProductAttributeBridge(productAttributeField, attributeValueField));
    }
}
