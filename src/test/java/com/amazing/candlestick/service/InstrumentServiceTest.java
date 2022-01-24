package com.amazing.candlestick.service;

import com.amazing.candlestick.TestHelper;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.dto.Instrument;
import com.amazing.candlestick.dto.Message;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.utility.JsonHelper;
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
class InstrumentServiceTest {

    @InjectMocks
    InstrumentService instrumentService;

    @Mock
    InstrumentRepository instrumentRepository;

    Optional<Message<Instrument>> instrumentAdd;
    Optional<Message<Instrument>> instrumentDelete;
    @BeforeEach
    void setUp() throws IOException {
        instrumentAdd    = JsonHelper.toInstrument(TestHelper.loadJsonFromFile("instrument-add"));
        instrumentDelete = JsonHelper.toInstrument(TestHelper.loadJsonFromFile("instrument-delete"));
    }

    @Test
    void whenReceiveAddMessage_saveInstrument() throws IOException {
        Mockito.when(instrumentRepository.save(Mockito.any(InstrumentEntity.class))).thenReturn(new InstrumentEntity());

        instrumentService.saveInstrument(instrumentAdd.get());
    }
    @Test
    void whenReceiveDeleteMessage_deleteInstrument() throws IOException {
        Mockito.when(instrumentRepository.findTopByIsinOrderByDate(Mockito.anyString())).thenReturn(Optional.of(new InstrumentEntity()));
        Mockito.doNothing().when(instrumentRepository).delete(Mockito.any(InstrumentEntity.class));


        instrumentService.saveInstrument(instrumentDelete.get());
    }

}