package com.amazing.candlestick.controller;

import com.amazing.candlestick.dto.CandlestickResponseDTO;
import com.amazing.candlestick.exception.NotFoundDataException;
import com.amazing.candlestick.service.CandlestickService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candlesticks")
public class CandlestickController {

    private final CandlestickService candlestickService;

    private CandlestickController(CandlestickService candlestickService) {
        this.candlestickService = candlestickService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCandlesticks(@RequestParam String isin) throws NotFoundDataException {
        CandlestickResponseDTO candlestickResponseDTO = candlestickService.getCandlesticks(isin);
        return new ResponseEntity<>(candlestickResponseDTO, HttpStatus.OK);
    }
}
