package com.amazing.candlestick.utility;

import com.amazing.candlestick.dto.Instrument;
import com.amazing.candlestick.dto.Message;
import com.amazing.candlestick.dto.Quote;
import com.amazing.candlestick.entity.InstrumentEntity;
import com.amazing.candlestick.entity.QuoteEntity;

public class Mapper {

    private Mapper() {}

    public static InstrumentEntity instrumentToEntity(Message<Instrument> instrumentMessage) {
        InstrumentEntity instrumentEntity = new InstrumentEntity();
        instrumentEntity.setDescription(instrumentMessage.getData().getDescription());
        instrumentEntity.setType(instrumentMessage.getType());
        instrumentEntity.setDate(DateTimeHelper.applyUTCZone(instrumentMessage.getDate()));
        instrumentEntity.setIsin(instrumentMessage.getData().getIsin());
        return instrumentEntity;
    }

    public static QuoteEntity quoteToEntity(Message<Quote> quoteMessage) {
        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.setIsin(quoteMessage.getData().getIsin());
        quoteEntity.setPrice(quoteMessage.getData().getPrice());
        quoteEntity.setDate(DateTimeHelper.applyUTCZone(quoteMessage.getDate()));
        return quoteEntity;
    }

}
