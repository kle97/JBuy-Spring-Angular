package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.Slice;

import java.io.IOException;

public class CustomSliceSerializer extends StdSerializer<Slice> {

    protected CustomSliceSerializer() {
        super(Slice.class);
    }

    @Override
    public void serialize(Slice slice,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("content");
        serializerProvider.defaultSerializeValue(slice.getContent(), jsonGenerator);
        jsonGenerator.writeObjectField("pageable", slice.getPageable());
        jsonGenerator.writeBooleanField("first", slice.isFirst());
        jsonGenerator.writeBooleanField("last", slice.isLast());
        jsonGenerator.writeNumberField("number", slice.getNumber());
        jsonGenerator.writeObjectField("sort", slice.getSort());
        jsonGenerator.writeNumberField("numberOfElements", slice.getNumberOfElements());
        jsonGenerator.writeNumberField("size", slice.getSize());
        jsonGenerator.writeBooleanField("empty", slice.isEmpty());
        jsonGenerator.writeEndObject();
    }
}
