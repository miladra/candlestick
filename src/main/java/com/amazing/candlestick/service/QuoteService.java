package com.amazing.candlestick.service;

import com.amazing.candlestick.dto.Message;
import com.amazing.candlestick.dto.Quote;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.entity.QuoteEntity;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.repository.QuoteRepository;
import com.amazing.candlestick.utility.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class QuoteService {

    private final InstrumentRepository instrumentRepository;
    private final QuoteRepository quoteRepository;

    public QuoteService(InstrumentRepository instrumentRepository, QuoteRepository quoteRepository) {
        this.instrumentRepository = instrumentRepository;
        this.quoteRepository = quoteRepository;
    }

    @Transactional
    public void saveQuote(Message<Quote> quoteMessage) {
        Optional<InstrumentEntity> instrumentEntity = instrumentRepository.findTopByIsinOrderByDate(quoteMessage.getData().getIsin());
        if (instrumentEntity.isPresent()) {
            QuoteEntity quoteEntity = Mapper.quoteToEntity(quoteMessage);
            quoteEntity.setInstrument(instrumentEntity.get());
            quoteRepository.save(quoteEntity);
        }
    }


}
