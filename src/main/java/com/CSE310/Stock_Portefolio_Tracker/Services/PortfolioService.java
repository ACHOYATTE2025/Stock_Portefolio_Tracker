package com.CSE310.Stock_Portefolio_Tracker.Services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.CSE310.Stock_Portefolio_Tracker.Dto.GlobalQuoteResponse;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PortfolioService {
    private final StockApiClient stockApiClient;

     public BigDecimal getCurrentValue(String symbol, int quantity) {
        GlobalQuoteResponse response = stockApiClient.getStockPrice(symbol);
        BigDecimal price = new BigDecimal(response.getQuote().getPrice());
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}
