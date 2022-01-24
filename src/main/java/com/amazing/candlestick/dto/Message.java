package com.amazing.candlestick.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Message<T> {

    private MessageType type;

    private Date date;

    private T data;

}
