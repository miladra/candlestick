package com.amazing.candlestick.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MessageType {
    ADD, DELETE, QUOTE;

    @JsonCreator
    public static MessageType forValue(String value) {
        return MessageType.valueOf(value.toUpperCase());
    }
}
