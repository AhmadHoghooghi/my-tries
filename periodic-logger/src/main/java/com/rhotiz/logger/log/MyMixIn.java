package com.rhotiz.logger.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.appenders.log4j2.elasticsearch.json.jackson.ExtendedLogEventJacksonJsonMixIn;

public abstract class MyMixIn extends ExtendedLogEventJacksonJsonMixIn {

    @JsonGetter("@timestamp")
    public String getTimeStamp() {
        Instant instant = Instant.ofEpochMilli(getTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }

}
