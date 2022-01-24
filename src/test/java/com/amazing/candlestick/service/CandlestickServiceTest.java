package com.amazing.candlestick.service;

import com.amazing.candlestick.TestHelper;
import com.amazing.candlestick.configuration.AppConfig;
import com.amazing.candlestick.dto.CandlestickResponseDTO;
import com.amazing.candlestick.exception.NotFoundDataException;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.utility.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CandlestickServiceTest {

    @InjectMocks
    CandlestickService candlestickService;


    @Mock
    InstrumentRepository instrumentRepository;

    @Mock
    AppConfig appConfig;

    Optional<InstrumentEntity> instrumentEntity;
    @BeforeEach
    void setUp() throws IOException {
        instrumentEntity = JsonHelper.toInstrumentEntity(TestHelper.loadJsonFromFile("instrumentWithQuote"));
    }

    @Test
    void whenQuotesAreInCurrentTime_generateCandlestick() throws NotFoundDataException {
        instrumentEntity.get().getQuotes().forEach(t-> t.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant())));

        Mockito.when(appConfig.getHistory()).thenReturn(30);
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(instrumentEntity);

        CandlestickResponseDTO candlestickResponseDTO = candlestickService.getCandlesticks("IV1724507OO2");
        assertThat(candlestickResponseDTO.getCandles().size()).isEqualTo(1);
    }

    @Test
    void whenQuotesAreInNotCurrentTime_generateCandlestick() throws NotFoundDataException {
        instrumentEntity.get().getQuotes().forEach(t-> t.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant().minus(2 , ChronoUnit.MINUTES ))));

        Mockito.when(appConfig.getHistory()).thenReturn(30);
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(instrumentEntity);

        CandlestickResponseDTO candlestickResponseDTO = candlestickService.getCandlesticks("IV1724507OO2");
        assertThat(candlestickResponseDTO.getCandles().size()).isEqualTo(1);
    }

    @Test
    void whenQuotesAreBeforHistory_expectedException() {
        instrumentEntity.get().getQuotes().forEach(t-> t.setDate(Date.from(Instant.now().atZone(ZoneOffset.UTC).toInstant().minus(2 , ChronoUnit.MINUTES ))));

        Mockito.when(appConfig.getHistory()).thenReturn(1);
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(instrumentEntity);

        Assertions.assertThrows(NotFoundDataException.class, () -> {
            candlestickService.getCandlesticks("IV1724507OO2");
        });

    }

    @Test
    void whenInstrumentQuoteInNull_expectedException() {
        instrumentEntity.get().setQuotes(null);

        Mockito.when(appConfig.getHistory()).thenReturn(1);
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(instrumentEntity);

        Assertions.assertThrows(NotFoundDataException.class, () -> {
            candlestickService.getCandlesticks("IV1724507OO2");
        });

    }
    @Test
    void whenInstrumentDoesNotContainQuote_expectedException() {
        instrumentEntity.get().setQuotes(new HashSet<>());

        Mockito.when(appConfig.getHistory()).thenReturn(1);
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(instrumentEntity);

        Assertions.assertThrows(NotFoundDataException.class, () -> {
            candlestickService.getCandlesticks("IV1724507OO2");
        });

    }
}