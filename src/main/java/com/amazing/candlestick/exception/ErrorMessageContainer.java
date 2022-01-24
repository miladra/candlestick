package com.amazing.candlestick.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessageContainer {
    private String errorMessage;
}