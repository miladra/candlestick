package com.amazing.candlestick.service;

import com.amazing.candlestick.configuration.AppConfig;
import com.amazing.candlestick.dto.Candlestick;
import com.amazing.candlestick.dto.CandlestickResponseDTO;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.entity.QuoteEntity;
import com.amazing.candlestick.exception.NotFoundDataException;
import com.amazing.candlestick.repository.InstrumentRepository;
import com.amazing.candlestick.utility.DateTimeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CandlestickService {

    private final InstrumentRepository instrumentRepository;
    private final AppConfig appConfig;

    @Autowired
    ObjectMapper objectMapper;

    public CandlestickService(InstrumentRepository instrumentRepository, AppConfig appConfig) {
        this.instrumentRepository = instrumentRepository;
        this.appConfig = appConfig;
    }

    public CandlestickResponseDTO getCandlesticks(String isin) throws NotFoundDataException {

        Map<Timestamp, Candlestick> candles = quotesGrouping(isin, appConfig.getHistory());



        CandlestickResponseDTO candlestickResponseDTO = new CandlestickResponseDTO();
        candlestickResponseDTO.setIsin(isin);
        candlestickResponseDTO.setCandles(candles.values().stream()
                                          .sorted(Comparator.comparing(Candlestick::getOpenTimestamp))
                                          .collect(Collectors.toList()));

        return candlestickResponseDTO;
    }


    private Map<Timestamp, Candlestick> quotesGrouping(String isin, int timeFrame) throws NotFoundDataException {

        Map<Timestamp, Candlestick> output = new HashMap();
        Optional<InstrumentEntity> instrumentEntities = instrumentRepository.findTopByIsinOrderByDate(isin);
        verifyQuotes(instrumentEntities);

        Date requestedDate = DateTimeHelper.minusMinutes(new Date(), timeFrame);
        List<QuoteEntity> quotes = instrumentEntities.get().getQuotes().stream()
                .filter(t -> t.getDate().toInstant().isAfter(requestedDate.toInstant()))
                .sorted(Comparator.comparing(QuoteEntity::getDate))
                .collect(Collectors.toList());

        if (quotes.size() == 0) {
            throw new NotFoundDataException(String.format("No data found for last %s minutes.", timeFrame));
        }

        // One minute windowing
        //2019-03-05 13:00:00 firstMinute
        //2019-03-05 13:00:59 lastMinute

        Date lastMinute = DateTimeHelper.adjustToLastMinute(quotes.get(0).getDate());
        Instant firstMinute = DateTimeHelper.adjustToFirstMinute(lastMinute).toInstant();

        List<QuoteEntity> partialQuote = new ArrayList<>();
        for (QuoteEntity quote : quotes) {
            if (lastMinute.toInstant().isAfter(quote.getDate().toInstant())) {
                partialQuote.add(quote);
            } else {
                GenerateCandlestick(output, quotes, lastMinute, firstMinute);
                partialQuote.clear();
                partialQuote.add(quote);
                lastMinute = DateTimeHelper.adjustToLastMinute(quote.getDate());
                firstMinute = DateTimeHelper.adjustToFirstMinute(lastMinute).toInstant();
            }
        }

        if (partialQuote.size() > 0) {
            GenerateCandlestick(output, quotes, lastMinute, firstMinute);
            partialQuote.clear();
        }
        return output;
    }

    private void verifyQuotes(Optional<InstrumentEntity> instrumentEntities) throws NotFoundDataException {

        if (!instrumentEntities.isPresent()) {
            throw new NotFoundDataException("Instrument not found");
        } else if (Objects.isNull(instrumentEntities.get().getQuotes()) || instrumentEntities.get().getQuotes().size() == 0) {
            throw new NotFoundDataException("Quotes not found");
        }
    }

    private void GenerateCandlestick(Map<Timestamp, Candlestick> output, List<QuoteEntity> quotes, Date lastMinute, Instant firstMinute) {
        Optional<QuoteEntity> max = quotes.stream().max(Comparator.comparing(QuoteEntity::getPrice));
        Optional<QuoteEntity> min = quotes.stream().min(Comparator.comparing(QuoteEntity::getPrice));
        QuoteEntity close = quotes.get(quotes.size() - 1);
        QuoteEntity open = quotes.get(0);
        output.put(Timestamp.from(firstMinute), new Candlestick(Timestamp.from(firstMinute), open.getPrice(), max.get().getPrice(), min.get().getPrice(), close.getPrice(), lastMinute));
    }

}
