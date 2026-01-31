package com.CSE310.Stock_Portefolio_Tracker.Dto;

import java.math.BigDecimal;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@Getter
@NoArgsConstructor
public class HoldingResponse {
    private String stockSymbol;
    private String stockName;
    private int quantity;
    private BigDecimal currentPrice ;

   
    public HoldingResponse(String symbol, String name, int quantity2, BigDecimal currentPrice2) {
        this.stockSymbol = symbol;
        this.stockName = name;
        this.quantity = quantity2;
        this.currentPrice = currentPrice2;

    }
  
}
