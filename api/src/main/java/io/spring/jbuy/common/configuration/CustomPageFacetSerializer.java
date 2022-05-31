package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.spring.jbuy.features.product.dto.PageFacet;

import java.io.IOException;

public class CustomPageFacetSerializer extends StdSerializer<PageFacet> {

    protected CustomPageFacetSerializer() {
        super(PageFacet.class);
    }

    @Override
    public void serialize(PageFacet pageFacet,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("content");
        serializerProvider.defaultSerializeValue(pageFacet.getContent(), jsonGenerator);
        jsonGenerator.writeObjectField("pageable", pageFacet.getPageable());
        jsonGenerator.writeNumberField("totalPages", pageFacet.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", pageFacet.getTotalElements());
        jsonGenerator.writeBooleanField("last", pageFacet.isLast());
        jsonGenerator.writeObjectField("sort", pageFacet.getSort());
        jsonGenerator.writeBooleanField("first", pageFacet.isFirst());
        jsonGenerator.writeNumberField("size", pageFacet.getSize());
        jsonGenerator.writeNumberField("number", pageFacet.getNumber());
        jsonGenerator.writeNumberField("numberOfElements", pageFacet.getNumberOfElements());
        jsonGenerator.writeBooleanField("empty", pageFacet.isEmpty());
        jsonGenerator.writeObjectField("facetMap", pageFacet.getFacetMap());
        jsonGenerator.writeEndObject();
    }
}
