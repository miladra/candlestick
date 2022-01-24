package com.amazing.candlestick.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CandlestickResponseDTO {

    private String isin;

    private List<Candlestick> candles;

}
