package com.amazing.candlestick.utility;

import com.amazing.candlestick.dto.Instrument;
import com.amazing.candlestick.dto.Quote;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazing.candlestick.dto.Message;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

public class JsonHelper {

    private JsonHelper() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Optional<Message<Instrument>> toInstrument(String payload) {
        try {
            Message<Instrument> instrumentMessage = MAPPER.readValue(payload, new TypeReference<>() {
            });
            if (instrumentMessage != null && instrumentMessage.getDate() == null) {
                instrumentMessage.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant()));
            }
            return Optional.ofNullable(instrumentMessage);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static Optional<Message<Quote>> toQuote(String payload) {
        try {
            Message<Quote> quoteMessage = MAPPER.readValue(payload, new TypeReference<>() {
            });
            if (quoteMessage != null && quoteMessage.getDate() == null) {
                quoteMessage.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant()));
            }
            return Optional.ofNullable(quoteMessage);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static Optional<InstrumentEntity> toInstrumentEntity(String payload) {
        try {
            InstrumentEntity instrumentMessage = MAPPER.readValue(payload, new TypeReference<>() {
            });
            return Optional.ofNullable(instrumentMessage);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
