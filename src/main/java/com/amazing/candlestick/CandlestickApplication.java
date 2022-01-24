package com.amazing.candlestick;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Candlestick API", version = "2.0", description = "Candlestick Code Challenge"))
public class CandlestickApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandlestickApplication.class, args);
    }
}