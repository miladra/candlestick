package com.amazing.candlestick.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {

    @Value("${partner.instrument.url}")
    private String instrumentsUrl;

    @Value("${partner.quote.url}")
    private String quotesUrl;


    @Value("${history}")
    private int history;

}
