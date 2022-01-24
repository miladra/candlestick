package com.amazing.candlestick.controller;

import com.amazing.candlestick.TestHelper;
import com.amazing.candlestick.dto.CandlestickResponseDTO;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.entity.QuoteEntity;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.utility.DateTimeHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazing.candlestick.repository.QuoteRepository;
import com.amazing.candlestick.utility.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CandlestickControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    ObjectMapper objectMapper;

    InstrumentEntity instrumentEntity;

    @BeforeEach
    void setUp() throws IOException {
        quoteRepository.deleteAll();
        instrumentRepository.deleteAll();
        instrumentEntity = JsonHelper.toInstrumentEntity(TestHelper.loadJsonFromFile("instrumentWithQuote")).get();
        instrumentRepository.save(instrumentEntity);
    }
    @Test
    public void contextLoads() {
    }

    @Test
    void whenReceiveEmptyIsin_shouldReturn4xx() throws Exception {
        mockMvc.perform(get("/candlesticks").contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    void whenQuotesAreInCurrentTime_generateCandlestick() throws Exception {

        InstrumentEntity instrumentEntitiesAfterSave = instrumentRepository.findTopByIsinOrderByDate("IV1724507OO2").get();
        instrumentEntity.getQuotes().forEach(t-> {
            t.setInstrument(instrumentEntitiesAfterSave);
            t.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant()));
         });
        quoteRepository.saveAll(instrumentEntity.getQuotes());

        String result = mockMvc.perform(get("/candlesticks?isin=IV1724507OO2").contentType("application/json"))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();

        CandlestickResponseDTO candlestickResponseDTO = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(candlestickResponseDTO.getCandles().size()).isEqualTo(1);
        assertThat(candlestickResponseDTO.getIsin()).isEqualTo("IV1724507OO2");

    }

    @Test
    void whenQuotesAreNotInCurrentTime_generateCandlestick() throws Exception {

        InstrumentEntity instrumentEntitiesAfterSave = instrumentRepository.findTopByIsinOrderByDate("IV1724507OO2").get();
        instrumentEntity.getQuotes().forEach(t-> {
            t.setInstrument(instrumentEntitiesAfterSave);
            t.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant().minus(2 , ChronoUnit.MINUTES)));
        });
        quoteRepository.saveAll(instrumentEntity.getQuotes());

        String result = mockMvc.perform(get("/candlesticks?isin=IV1724507OO2").contentType("application/json"))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();

        CandlestickResponseDTO candlestickResponseDTO = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(candlestickResponseDTO.getCandles().size()).isEqualTo(1);
        assertThat(candlestickResponseDTO.getIsin()).isEqualTo("IV1724507OO2");
    }
    @Test
    void whenQuotesAreInFirstOrLastMinute_generateCandlestick() throws Exception {

        InstrumentEntity instrumentEntitiesAfterSave = instrumentRepository.findTopByIsinOrderByDate("IV1724507OO2").get();
        Set<QuoteEntity> quotes = new HashSet<>();


        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setInstrument(instrumentEntitiesAfterSave);
        quoteEntity.setIsin("IV1724507OO2");
        quoteEntity.setPrice(1200);
        quoteEntity.setDate(DateTimeHelper.adjustToFirstMinute(new Date()));
        quotes.add(quoteEntity);


        quoteEntity = new QuoteEntity();
        quoteEntity.setInstrument(instrumentEntitiesAfterSave);
        quoteEntity.setIsin("IV1724507OO2");
        quoteEntity.setPrice(1201);
        quoteEntity.setDate(DateTimeHelper.adjustToLastMinute(new Date()));
        quotes.add(quoteEntity);


        quoteEntity = new QuoteEntity();
        quoteEntity.setInstrument(instrumentEntitiesAfterSave);
        quoteEntity.setIsin("IV1724507OO2");
        quoteEntity.setPrice(1202);
        quoteEntity.setDate(DateTimeHelper.adjustToFirstNextMinute(new Date()));
        quotes.add(quoteEntity);

        quoteRepository.saveAll(quotes);

        String result = mockMvc.perform(get("/candlesticks?isin=IV1724507OO2").contentType("application/json"))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();

        CandlestickResponseDTO candlestickResponseDTO = objectMapper.readValue(result, new TypeReference<>() {
        });

        assertThat(candlestickResponseDTO.getCandles().size()).isEqualTo(2);
        assertThat(candlestickResponseDTO.getIsin()).isEqualTo("IV1724507OO2");
    }
}