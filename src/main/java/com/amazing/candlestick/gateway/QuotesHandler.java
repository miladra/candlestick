package com.amazing.candlestick.gateway;

import com.amazing.candlestick.dto.Quote;
import com.amazing.candlestick.service.QuoteService;
import com.amazing.candlestick.dto.Message;
import com.amazing.candlestick.service.InstrumentService;
import com.amazing.candlestick.utility.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
@Slf4j
public class QuotesHandler implements WebSocketHandler {


    private final InstrumentService instrumentService;
    private final QuoteService quoteService;

    public QuotesHandler(InstrumentService instrumentService,QuoteService quoteService) {
        this.instrumentService = instrumentService;
        this.quoteService = quoteService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Quote connection was established. session id: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            Optional<Message<Quote>> quote = JsonHelper.toQuote(message.getPayload().toString());
            quote.ifPresent(quoteService::saveQuote);
        }catch (Exception ex){
            log.error("Exception occurred." , ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Exception occurred.", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("Connection closed. remove all");
        instrumentService.removeAll();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
