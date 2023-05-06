package ua.illia.estore.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

public class PageSerializer extends StdSerializer<PageImpl> {

    public PageSerializer() {
        super(PageImpl.class);
    }

    @Override
    public void serialize(PageImpl value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("content");
        provider.defaultSerializeValue(value.getContent(), gen);
        gen.writeFieldName("pageable");
        provider.defaultSerializeValue(value.getPageable(), gen);
        gen.writeNumberField("totalPages", value.getTotalPages());
        gen.writeNumberField("totalElements", value.getTotalElements());
        gen.writeBooleanField("last", value.isLast());
        gen.writeNumberField("size", value.getSize());
        gen.writeNumberField("number", value.getNumber());
        gen.writeFieldName("sort");
        provider.defaultSerializeValue(value.getSort(), gen);
        gen.writeBooleanField("first", value.isFirst());
        gen.writeNumberField("numberOfElements", value.getNumberOfElements());
        gen.writeBooleanField("empty", value.isEmpty());
        gen.writeEndObject();
    }
}
