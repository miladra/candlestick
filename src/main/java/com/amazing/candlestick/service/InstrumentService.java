package com.amazing.candlestick.service;

import com.amazing.candlestick.dto.MessageType;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.dto.Instrument;
import com.amazing.candlestick.dto.Message;
import com.amazing.candlestick.repository.QuoteRepository;
import com.amazing.candlestick.utility.Mapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Data
@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final QuoteRepository quoteRepository;

    public InstrumentService(InstrumentRepository instrumentRepository, QuoteRepository quoteRepository) {
        this.instrumentRepository = instrumentRepository;
        this.quoteRepository = quoteRepository;
    }

    @Transactional
    public void saveInstrument(Message<Instrument> instrumentMessage) {
        if (instrumentMessage.getType().equals(MessageType.ADD)) {
            instrumentRepository.save(Mapper.instrumentToEntity(instrumentMessage));
        } else if (instrumentMessage.getType().equals(MessageType.DELETE)) {
            Optional<InstrumentEntity> lastInstrument = instrumentRepository.findTopByIsinOrderByDate(instrumentMessage.getData().getIsin());
            if (lastInstrument.isPresent()) {
                instrumentRepository.delete(lastInstrument.get());
            }
        }
    }

    @Transactional
    public void removeAll() {
        quoteRepository.deleteAll();
        instrumentRepository.deleteAll();
    }

}
