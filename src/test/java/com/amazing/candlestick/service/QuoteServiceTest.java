package com.amazing.candlestick.service;

import com.amazing.candlestick.TestHelper;
import com.amazing.candlestick.dto.Quote;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.entity.QuoteEntity;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.repository.QuoteRepository;
import com.amazing.candlestick.utility.JsonHelper;
import com.amazing.candlestick.dto.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @InjectMocks
    QuoteService quoteService;

    @Mock
    QuoteRepository quoteRepository;

    @Mock
    InstrumentRepository instrumentRepository;

    Optional<Message<Quote>> quote;
    @BeforeEach
    void setUp() throws IOException {
         quote = JsonHelper.toQuote(TestHelper.loadJsonFromFile("quote"));
    }

    @Test
    void whenReceiveMessage_saveQuote() throws IOException {
        Mockito.when(quoteRepository.save(Mockito.any(QuoteEntity.class))).thenReturn(new QuoteEntity());
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(Optional.of(new InstrumentEntity()));


        quoteService.saveQuote(quote.get());
    }
}