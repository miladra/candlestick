package com.amazing.candlestick.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candlestick {

    private Date openTimestamp;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private Date closeTimestamp;

}
