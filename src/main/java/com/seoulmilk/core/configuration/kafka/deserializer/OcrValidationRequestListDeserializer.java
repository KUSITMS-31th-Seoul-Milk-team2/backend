package com.seoulmilk.core.configuration.kafka.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.seoulmilk.receipt.dto.request.OcrValidationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OcrValidationRequestListDeserializer extends StdDeserializer<List<OcrValidationRequest>> {

    public OcrValidationRequestListDeserializer() {
        this(null);
    }

    public OcrValidationRequestListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<OcrValidationRequest> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<OcrValidationRequest> result = new ArrayList<>();

        if (p.currentToken() == JsonToken.START_ARRAY) {
            p.nextToken();

            if (p.currentToken() == JsonToken.START_ARRAY) {
                while (p.nextToken() != JsonToken.END_ARRAY) {
                    result.add(deserializeSingleRequest(p, ctxt));
                }
                p.nextToken();
            }

            else {
                while (p.currentToken() != JsonToken.END_ARRAY) {
                    result.add(deserializeSingleRequest(p, ctxt));
                    p.nextToken();
                }
            }
        }

        else if (p.currentToken() == JsonToken.START_OBJECT) {
            result.add(deserializeSingleRequest(p, ctxt));
        }

        return result;
    }

    private OcrValidationRequest deserializeSingleRequest(JsonParser p, DeserializationContext ctxt) throws IOException {
        JavaType type = ctxt.getTypeFactory().constructType(OcrValidationRequest.class);
        return ctxt.readValue(p, type);
    }
}
