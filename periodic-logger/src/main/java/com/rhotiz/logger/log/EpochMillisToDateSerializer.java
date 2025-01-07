package com.rhotiz.logger.log;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EpochMillisToDateSerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long epochMillis, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        Instant instant = Instant.ofEpochMilli(epochMillis);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);
        String formattedDate = formatter.format(instant);
        jsonGenerator.writeString(formattedDate);
//        jsonGenerator.writeFieldName("@timestamp");
    }
}