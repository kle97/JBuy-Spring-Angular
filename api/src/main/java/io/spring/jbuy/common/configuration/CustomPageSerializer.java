package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class CustomPageSerializer extends StdSerializer<Page> {

    protected CustomPageSerializer() {
        super(Page.class);
    }

    @Override
    public void serialize(Page page,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("content");
        serializerProvider.defaultSerializeValue(page.getContent(), jsonGenerator);
        jsonGenerator.writeObjectField("pageable", page.getPageable());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeBooleanField("last", page.isLast());
        jsonGenerator.writeObjectField("sort", page.getSort());
        jsonGenerator.writeBooleanField("first", page.isFirst());
        jsonGenerator.writeNumberField("size", page.getSize());
        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());
        jsonGenerator.writeBooleanField("empty", page.isEmpty());
        jsonGenerator.writeEndObject();
    }
}
