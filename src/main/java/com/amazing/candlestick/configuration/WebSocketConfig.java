package com.amazing.candlestick.configuration;


import com.amazing.candlestick.gateway.InstrumentHandler;
import com.amazing.candlestick.gateway.QuotesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebSocketConfig {

    private final AppConfig appConfig;
    private final InstrumentHandler instrumentHandler;
    private final QuotesHandler quotesHandler;

    public WebSocketConfig(AppConfig appConfig,
                           InstrumentHandler instrumentHandler,
                           QuotesHandler quotesHandler) {
        this.appConfig = appConfig;
        this.instrumentHandler = instrumentHandler;
        this.quotesHandler = quotesHandler;
    }

    @Bean
    public WebSocketConnectionManager wsInstrumentsConnectionManager() {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                instrumentHandler,
                appConfig.getInstrumentsUrl());

        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public WebSocketConnectionManager wsQuotesConnectionManager() {

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                quotesHandler,
                appConfig.getQuotesUrl());

        manager.setAutoStartup(true);
        return manager;
    }

}
