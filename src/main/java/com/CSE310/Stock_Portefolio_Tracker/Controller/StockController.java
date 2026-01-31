package com.CSE310.Stock_Portefolio_Tracker.Controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CSE310.Stock_Portefolio_Tracker.Services.PortfolioService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StockController {

     private final PortfolioService portfolioService;


        @GetMapping("/{symbol}/{quantity}")
        public BigDecimal getStockValue(
                @PathVariable String symbol,
                @PathVariable int quantity) {

            return portfolioService.getCurrentValue(symbol, quantity);
        }

}
